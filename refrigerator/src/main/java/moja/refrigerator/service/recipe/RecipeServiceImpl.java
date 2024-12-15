package moja.refrigerator.service.recipe;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import moja.refrigerator.aggregate.recipe.Recipe;
import moja.refrigerator.aggregate.recipe.RecipeCategory;
import moja.refrigerator.aggregate.recipe.RecipeSource;
import moja.refrigerator.aggregate.recipe.RecipeSourceType;
import moja.refrigerator.aggregate.user.User;
import moja.refrigerator.dto.ingredient.response.IngredientResponse;
import moja.refrigerator.dto.recipe.request.RecipeCreateRequest;
import moja.refrigerator.dto.recipe.request.RecipeUpdateRequest;
import moja.refrigerator.dto.recipe.response.RecipeDetailResponse;
import moja.refrigerator.dto.recipe.response.RecipeResponse;
import moja.refrigerator.repository.recipe.RecipeCategoryRepository;
import moja.refrigerator.repository.recipe.RecipeRepository;
import moja.refrigerator.repository.recipe.RecipeSourceRepository;
import moja.refrigerator.repository.recipe.RecipeSourceTypeRepository;
import moja.refrigerator.repository.user.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RecipeServiceImpl implements RecipeService {

    private RecipeRepository recipeRepository;
    private UserRepository userRepository;
    private RecipeSourceRepository recipeSourceRepository;
    private RecipeCategoryRepository recipeCategoryRepository;
    private ModelMapper mapper;
    private RecipeSourceTypeRepository recipeSourceTypeRepository;
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // 이미지의 확장자를 확인, 이를 이용해 타입을 분류한다.
    private boolean isImageFile(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        return List.of("jpg", "jpeg", "png", "gif").contains(extension);
    }
    // 동영상의 확장자를 확인, 이를 이용해 타입을 분류한다.
    private boolean isVideoFile(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        return List.of("mp4", "avi", "mov", "wmv").contains(extension);
    }
    // 2가지 동시에 다 쓰는 이유는, 둘다 아닐 경우(ex] exe, java 등 잘못된 확장자를 올릴 시 메시지를 주기 위해)

    @Autowired
    public RecipeServiceImpl(
            RecipeRepository recipeRepository,
            ModelMapper mapper,
            RecipeSourceRepository recipeSourceRepository,
            UserRepository userRepository,
            RecipeCategoryRepository recipeCategoryRepository,
            RecipeSourceTypeRepository recipeSourceTypeRepository,
            AmazonS3Client amazonS3Client
    ) {
        this.recipeRepository = recipeRepository;
        this.mapper = mapper;
        this.recipeSourceRepository = recipeSourceRepository;
        this.userRepository = userRepository;
        this.recipeCategoryRepository = recipeCategoryRepository;
        this.recipeSourceTypeRepository=recipeSourceTypeRepository;
        this.amazonS3Client = amazonS3Client;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void createRecipe(RecipeCreateRequest request
            ,List<MultipartFile> files
    ) {
        Recipe recipe = new Recipe(); // mapper를 통한인식이 잘 안됨. -> 그냥 일일이 추가.
        recipe.setRecipeName(request.getRecipeName());
        recipe.setRecipeContent(request.getRecipeContent());
        recipe.setRecipeDifficulty(request.getRecipeDifficulty());
        recipe.setRecipeCookingTime(request.getRecipeCookingTime());


//        User 조회
        User user = userRepository.findById(request.getUserPk())
                        .orElseThrow(IllegalArgumentException::new);
        recipe.setUser(user);

//        RecipeCategory
        RecipeCategory recipeCategory = recipeCategoryRepository.findById(request.getRecipeCategoryPk())
                .orElseThrow(IllegalArgumentException::new);
        recipe.setRecipeCategory(recipeCategory);
        //RecipeSource 추가부분
        if(files != null && !files.isEmpty()) {
            for(MultipartFile file : files){
                try{// 1. 작성할 내용 정리
                    String recipeSourceFileName = file.getOriginalFilename();
                    UUID uuid = UUID.randomUUID();
                    String recipeSourceServername = uuid+recipeSourceFileName;

                    String recipeSourceSave = "https://"+bucket+"/recipe/"+recipeSourceServername;


                    // 2. 작성한 내용 저장
                    RecipeSource recipeSource = new RecipeSource();
                    recipeSource.setRecipeSourceServername(recipeSourceServername);
                    recipeSource.setRecipeSourceSave(recipeSourceSave);
                    recipeSource.setRecipeSourceFileName(recipeSourceFileName);


                    // 3. 자료 타입 가져오기.

                    RecipeSourceType recipeSourceType;
                    if(isImageFile(recipeSourceFileName)){
                        recipeSourceType = recipeSourceTypeRepository.findById(1)
                                .orElseThrow(IllegalArgumentException::new);
                    }else if (isVideoFile(recipeSourceFileName)){
                        recipeSourceType = recipeSourceTypeRepository.findById(2)
                                .orElseThrow(IllegalArgumentException::new);
                    }else{
                        throw new IllegalArgumentException("Unsupported file type");
                    }

                    // 아마존 서버 올리기
                    ObjectMetadata objectMetadata = new ObjectMetadata();
                    objectMetadata.setContentType(file.getContentType());
                    objectMetadata.setContentLength(file.getSize());
                    amazonS3Client.putObject(bucket,recipeSourceServername,file.getInputStream(),objectMetadata);

                    // 레시피 db에 저장.
                    recipeSource.setRecipeSourceType(recipeSourceType);
                    recipeSource.setRecipe(recipe);
                    recipeSourceRepository.save(recipeSource);
                    break;
                }catch (IOException e){
                    System.out.println(e.getMessage());
                }

            }
        }

        recipeRepository.save(recipe);
    }

    @Override
    public List<RecipeResponse> getAllRecipes(){
        return recipeRepository.findAll().stream()
                .map(recipe -> mapper.map(recipe, RecipeResponse.class))
                .collect(Collectors.toList());
    }

    public RecipeDetailResponse getRecipe(long id){
        Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("recipe not found"));
        RecipeDetailResponse response = mapper.map(recipe,RecipeDetailResponse.class);
        return response;
    }

                                          @Override
    public void deleteRecipe(long recipePk) {
        Recipe recipe = recipeRepository.findByRecipePk(recipePk)
                .orElseThrow(IllegalArgumentException::new);
        List<RecipeSource> sources = recipe.getRecipeSource();
        if(sources!=null && !sources.isEmpty()){
            for (RecipeSource recipeSource : sources) {
                amazonS3Client.deleteObject( new DeleteObjectRequest(bucket,recipeSource.getRecipeSourceServername()));
            }
        }
        recipeRepository.delete(recipe);

    }

    @Override
    @Transactional
    public void updateRecipe(RecipeUpdateRequest request, List<MultipartFile> files) {
        Recipe recipe = recipeRepository.findByRecipePk(request.getRecipePk())
                .orElseThrow(() -> new IllegalArgumentException("Recipe not found"));

        // 기본 필드 업데이트
        if (request.getRecipeName() != null) recipe.setRecipeName(request.getRecipeName());
        if (request.getRecipeCookingTime() != 0) recipe.setRecipeCookingTime(request.getRecipeCookingTime());
        if (request.getRecipeDifficulty() != 0) recipe.setRecipeDifficulty(request.getRecipeDifficulty());
        if (request.getRecipeCategory() != null) {
            recipe.setRecipeCategory(recipeCategoryRepository.findByRecipeCategory(request.getRecipeCategory())
                    .orElseThrow(() -> new IllegalArgumentException("Category not found")));
        }

        // 기존 RecipeSource와 업로드된 파일 비교
        List<RecipeSource> sources = recipe.getRecipeSource();
        List<String> uploadedFileNames = files.stream()
                .map(MultipartFile::getOriginalFilename)
                .toList();

        // 삭제할 RecipeSource
        List<RecipeSource> recipeSourcesToDelete = sources.stream()
                .filter(source -> !uploadedFileNames.contains(source.getRecipeSourceFileName()))
                .toList();
        for (RecipeSource recipeSource  : recipeSourcesToDelete) {
            amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, recipeSource.getRecipeSourceServername()));
            recipe.getRecipeSource().remove(recipeSource); // 부모 엔티티에서 참조 제거
            recipeSourceRepository.delete(recipeSource );
        }

        // 추가할 새 파일
        List<String> existingFileNames = sources.stream()
                .map(RecipeSource::getRecipeSourceFileName)
                .toList();

        List<MultipartFile> filesToAdd = files.stream()
                .filter(file -> !existingFileNames.contains(file.getOriginalFilename()))
                .toList();

        for (MultipartFile file : filesToAdd) {
            try {
                // 새 RecipeSource 생성
                String recipeSourceFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                String recipeSourceServername = recipeSourceFileName;
                String recipeSourceSave = "https://" + bucket + "/recipe/" + recipeSourceServername;

                ObjectMetadata objectMetadata = new ObjectMetadata();
                objectMetadata.setContentType(file.getContentType());
                objectMetadata.setContentLength(file.getSize());
                amazonS3Client.putObject(bucket, recipeSourceServername, file.getInputStream(), objectMetadata);

                RecipeSource recipeSource = new RecipeSource();
                recipeSource.setRecipeSourceServername(recipeSourceServername);
                recipeSource.setRecipeSourceSave(recipeSourceSave);
                recipeSource.setRecipeSourceFileName(file.getOriginalFilename());

                RecipeSourceType recipeSourceType;
                if (isImageFile(file.getOriginalFilename())) {
                    recipeSourceType = recipeSourceTypeRepository.findById(1)
                            .orElseThrow(() -> new IllegalArgumentException("Image type not found"));
                } else if (isVideoFile(file.getOriginalFilename())) {
                    recipeSourceType = recipeSourceTypeRepository.findById(2)
                            .orElseThrow(() -> new IllegalArgumentException("Video type not found"));
                } else {
                    throw new IllegalArgumentException("Unsupported file type");
                }

                recipeSource.setRecipeSourceType(recipeSourceType);
                recipeSource.setRecipe(recipe);
                recipeSourceRepository.save(recipeSource);
            } catch (IOException e) {
                throw new RuntimeException("Error processing file: " + file.getOriginalFilename(), e);
            }
        }

        // 최종 저장
        recipeRepository.save(recipe);
    }

}