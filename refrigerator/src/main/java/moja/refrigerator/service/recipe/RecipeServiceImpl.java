package moja.refrigerator.service.recipe;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import moja.refrigerator.aggregate.ingredient.IngredientManagement;
import moja.refrigerator.aggregate.ingredient.IngredientMyRefrigerator;
import moja.refrigerator.aggregate.recipe.*;
import moja.refrigerator.aggregate.user.User;
import moja.refrigerator.dto.recipe.RecipeMatchResult;
import moja.refrigerator.dto.recipe.request.*;
import moja.refrigerator.dto.recipe.response.*;
import moja.refrigerator.repository.ingredient.IngredientManagementRepository;
import moja.refrigerator.repository.ingredient.IngredientMyRefrigeratorRepository;
import moja.refrigerator.repository.recipe.*;
import moja.refrigerator.repository.user.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of the RecipeService interface, providing business logic for managing recipes.
 * This class interacts with various repositories for CRUD operations related to recipes, ingredients, files, and user data,
 * as well as Amazon S3 for file storage.
 */
@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final RecipeSourceRepository recipeSourceRepository;
    private final RecipeCategoryRepository recipeCategoryRepository;
    private final ModelMapper mapper;
    private final RecipeSourceTypeRepository recipeSourceTypeRepository;
    private final AmazonS3Client amazonS3Client;
    private final IngredientMyRefrigeratorRepository ingredientMyRefrigeratorRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final ReplacableIngredientRepository replacableIngredientRepository;
    private final RecipeLikeDislikeRepository recipeLikeDislikeRepository;
    private final RecipeStepRepository recipeStepRepository;
    private final RecipeStepSourceRepository recipeStepSourceRepository;
    private final IngredientManagementRepository ingredientManagementRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${cloud.aws.region.static}")
    private String region;

    @Autowired
    public RecipeServiceImpl(
            RecipeRepository recipeRepository,
            UserRepository userRepository,
            RecipeSourceRepository recipeSourceRepository,
            RecipeCategoryRepository recipeCategoryRepository,
            ModelMapper mapper,
            RecipeSourceTypeRepository recipeSourceTypeRepository,
            AmazonS3Client amazonS3Client,
            IngredientMyRefrigeratorRepository ingredientMyRefrigeratorRepository,
            RecipeIngredientRepository recipeIngredientRepository,
            ReplacableIngredientRepository replacableIngredientRepository,
            RecipeLikeDislikeRepository recipeLikeDislikeRepository,
            RecipeStepRepository recipeStepRepository,
            RecipeStepSourceRepository recipeStepSourceRepository,
            IngredientManagementRepository ingredientManagementRepository) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
        this.recipeSourceRepository = recipeSourceRepository;
        this.recipeCategoryRepository = recipeCategoryRepository;
        this.mapper = mapper;
        this.recipeSourceTypeRepository = recipeSourceTypeRepository;
        this.amazonS3Client = amazonS3Client;
        this.ingredientMyRefrigeratorRepository = ingredientMyRefrigeratorRepository;
        this.recipeIngredientRepository = recipeIngredientRepository;
        this.replacableIngredientRepository = replacableIngredientRepository;
        this.recipeLikeDislikeRepository = recipeLikeDislikeRepository;
        this.recipeStepRepository = recipeStepRepository;
        this.recipeStepSourceRepository = recipeStepSourceRepository;
        this.ingredientManagementRepository = ingredientManagementRepository;
    }
    private boolean isImageFile(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        return List.of("jpg", "jpeg", "png", "gif").contains(extension);
    }

    private boolean isVideoFile(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        return List.of("mp4", "avi", "mov", "wmv").contains(extension);
    }
    // 파일 처리 전용 메서드
    private RecipeSource RecipeSourceSaveFile(MultipartFile file, Recipe recipe, String pathPrefix) {
        try {
            if (file == null || file.isEmpty()) {
                return null;
            }
            String originalFileName = file.getOriginalFilename();
            if (originalFileName == null) {
                throw new IllegalArgumentException("Empty file name is not allowed");
            }

            // 파일 이름 생성 및 저장 경로 설정
            UUID uuid = UUID.randomUUID();
            String serverFileName = uuid + "_" + originalFileName;
            String fileUrl = "https://" + bucket + ".s3."+region+".amazonaws.com/" + pathPrefix + serverFileName;

            // RecipeSource 생성 및 설정
            RecipeSource recipeSource = new RecipeSource();
            recipeSource.setRecipeSourceServername(serverFileName);
            recipeSource.setRecipeSourceSave(fileUrl);
            recipeSource.setRecipeSourceFileName(originalFileName);

            // 파일 타입 처리
            RecipeSourceType recipeSourceType;
            if (isImageFile(originalFileName)) {
                recipeSourceType = recipeSourceTypeRepository.findById(1)
                        .orElseThrow(() -> new IllegalArgumentException("Image type not found"));
            } else if (isVideoFile(originalFileName)) {
                recipeSourceType = recipeSourceTypeRepository.findById(2)
                        .orElseThrow(() -> new IllegalArgumentException("Video type not found"));
            } else {
                throw new IllegalArgumentException("Unsupported file type: " + originalFileName);
            }
            recipeSource.setRecipeSourceType(recipeSourceType);
            recipeSource.setRecipe(recipe);


            // AWS S3에 파일 업로드
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());
            amazonS3Client.putObject(bucket, pathPrefix + serverFileName, file.getInputStream(), metadata);

            // RecipeSource 저장
            recipeSourceRepository.save(recipeSource);
            return recipeSource;
        } catch (IOException e) {
            throw new RuntimeException("File upload failed: " + e.getMessage(), e);
        }
    }

    private RecipeStepSource RecipeStepSourceSaveFile(MultipartFile file, RecipeStep recipeStep, String pathPrefix) {
        try {
            if (file == null || file.isEmpty()) {
                return null;
            }
            String originalFileName = file.getOriginalFilename();
            if (originalFileName == null) {
                throw new IllegalArgumentException("Empty file name is not allowed");
            }

            // 파일 이름 생성 및 저장 경로 설정
            UUID uuid = UUID.randomUUID();
            String serverFileName = uuid + "_" + originalFileName;
            String fileUrl = "https://" + bucket +".s3."+region+".amazonaws.com/"+  pathPrefix + serverFileName;

            // RecipeSource 생성 및 설정
            RecipeStepSource recipeSource = new RecipeStepSource();
            recipeSource.setRecipeStepSourceServername(serverFileName);
            recipeSource.setRecipeStepSourceSave(fileUrl);
            recipeSource.setRecipeStepSourceFileName(originalFileName);

            // 파일 타입 처리
            RecipeSourceType recipeSourceType;
            System.out.println(originalFileName+"-----------------------------------------------------------------");
            if (isImageFile(originalFileName)) {
                recipeSourceType = recipeSourceTypeRepository.findById(1)
                        .orElseThrow(() -> new IllegalArgumentException("Image type not found"));
            } else if (isVideoFile(originalFileName)) {
                recipeSourceType = recipeSourceTypeRepository.findById(2)
                        .orElseThrow(() -> new IllegalArgumentException("Video type not found"));
            } else {
                throw new IllegalArgumentException("Unsupported file type: " + originalFileName);
            }
            recipeSource.setRecipeStepSourceType(recipeSourceType);
            recipeSource.setRecipeStep(recipeStep);

            // AWS S3에 파일 업로드
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());
            amazonS3Client.putObject(bucket, pathPrefix + serverFileName, file.getInputStream(), metadata);

            // RecipeSource 저장
            recipeStepSourceRepository.save(recipeSource);
            return recipeSource;

        } catch (IOException e) {
            throw new RuntimeException("File upload failed: " + e.getMessage(), e);
        }
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Recipe createRecipe(RecipeCreateRequest request
            ,List<MultipartFile> recipeSources
            ,List<MultipartFile> recipeStepSources
    ) {
        Recipe recipe = new Recipe();
        recipe.setRecipeName(request.getRecipeName());
        recipe.setRecipeContent(request.getRecipeContent());
        recipe.setRecipeDifficulty(request.getRecipeDifficulty());
        recipe.setRecipeCookingTime(request.getRecipeCookingTime());

        User user = userRepository.findById(request.getUserPk())
                .orElseThrow(IllegalArgumentException::new);
        recipe.setUser(user);

        RecipeCategory recipeCategory = recipeCategoryRepository.findById(request.getRecipeCategoryPk())
                .orElseThrow(IllegalArgumentException::new);
        recipe.setRecipeCategory(recipeCategory);

        List<RecipeIngredientCreateRequest> recipeIngredient = request.getRecipeIngredients();
        if(recipeIngredient != null && !recipeIngredient.isEmpty()) {
            for (int i = 0; i < recipeIngredient.size(); i++) {
                RecipeIngredientCreateRequest ingredient = recipeIngredient.get(i);

                IngredientManagement ingredientManage =  ingredientManagementRepository.findByIngredientName(ingredient.getIngredientName())
                        .orElseThrow(IllegalArgumentException::new);

                System.out.println(ingredient.getIngredientName());

                RecipeIngredient newIngredient = new RecipeIngredient();
                newIngredient.setRecipe(recipe);
                newIngredient.setIngredientManagement(ingredientManage);
                newIngredient.setIngredientIsNecessary(ingredient.isIngredientIsNecessary());


                recipe.getRecipeIngredients().add(newIngredient);
            }
        }

        if(recipeSources != null && !recipeSources.isEmpty()) {
            for(MultipartFile file : recipeSources) {
                RecipeSource recipeSource = RecipeSourceSaveFile(file, recipe, "recipe/");
                recipe.getRecipeSource().add(recipeSource);
            }
        }


        List<RecipeStepRequest> recipeSteps = request.getRecipeSteps();
        if(recipeSteps != null ) {
            for (int i = 0; i < recipeSteps.size(); i++) {
                RecipeStepRequest step = recipeSteps.get(i);

                // RecipeStep 저장
                RecipeStep newStep = new RecipeStep();
                newStep.setRecipeStepOrder(i + 1);
                newStep.setRecipeStepContent(step.getRecipeStepContent());
                newStep.setRecipe(recipe);

                if (recipeStepSources != null && recipeStepSources.size() > i) {
                    MultipartFile file = recipeStepSources.get(i);
                    RecipeStepSource stepSource = RecipeStepSourceSaveFile(file, newStep, "recipe/step/");
                    newStep.setRecipeStepSource(stepSource); // 파일 매핑
                }

                recipe.getRecipeStep().add(newStep);
            }
        }
        recipeRepository.save(recipe);
        return recipe;
    }

    @Override
    public List<RecipeResponse> getAllRecipes() {
        return recipeRepository.findAll().stream()
                .map(recipe -> mapper.map(recipe, RecipeResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public RecipeDetailResponse getRecipe(long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("recipe not found"));
        return mapper.map(recipe, RecipeDetailResponse.class);
    }

    @Override
    public List<RecipeCategoryResponse> getRecipeCategory(){
        return recipeCategoryRepository.findAll().stream()
                .map(recipeCategory -> mapper.map(recipeCategory, RecipeCategoryResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteRecipe(long recipePk) {
        Recipe recipe = recipeRepository.findByRecipePk(recipePk)
                .orElseThrow(IllegalArgumentException::new);
        List<RecipeSource> sources = recipe.getRecipeSource();
        if(sources != null && !sources.isEmpty()) {
            for (RecipeSource recipeSource : sources) {
                amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, "recipe/"+recipeSource.getRecipeSourceServername()));
            }
        }
        List<RecipeStep> steps = recipe.getRecipeStep();
        if(steps != null && !steps.isEmpty()) {
            for (RecipeStep recipeStep : steps) {
                RecipeStepSource stepSource = recipeStep.getRecipeStepSource();
                if(stepSource != null) {
                    amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, "recipe/step/"+stepSource.getRecipeStepSourceServername()));
                }
            }
        }
        recipeRepository.delete(recipe);
    }



    @Override
    @Transactional
    public Recipe updateRecipe(RecipeUpdateRequest request
            ,List<MultipartFile> recipeSources
            ,List<MultipartFile> recipeStepSources
    ) {
        Recipe recipe = recipeRepository.findByRecipePk(request.getRecipePk())
                .orElseThrow(() -> new IllegalArgumentException("Recipe not found"));

        if (request.getRecipeName() != null) recipe.setRecipeName(request.getRecipeName());
        if (request.getRecipeCookingTime() != 0) recipe.setRecipeCookingTime(request.getRecipeCookingTime());
        if (request.getRecipeDifficulty() != 0) recipe.setRecipeDifficulty(request.getRecipeDifficulty());
        if (request.getRecipeCategoryPk() != 0) {
            recipe.setRecipeCategory(recipeCategoryRepository.findById(request.getRecipeCategoryPk())
                    .orElseThrow(() -> new IllegalArgumentException("Category not found")));
        }

        List<RecipeSource> sources = recipe.getRecipeSource();
        List<String> uploadedFileNames = recipeSources.stream()
                .map(MultipartFile::getOriginalFilename)
                .toList();

        List<RecipeSource> recipeSourcesToDelete = sources.stream()
                .filter(source -> !uploadedFileNames.contains(source.getRecipeSourceFileName()))
                .toList();

        recipe.getRecipeIngredients().clear();

        List<RecipeIngredientUpdateRequest> recipeIngredient = request.getRecipeIngredients();
        if(recipeIngredient != null && !recipeIngredient.isEmpty()) {
            for (int i = 0; i < recipeIngredient.size(); i++) {
                RecipeIngredientUpdateRequest ingredient = recipeIngredient.get(i);
                IngredientManagement ingredientManage =  ingredientManagementRepository.findByIngredientName(ingredient.getIngredientName())
                        .orElseThrow(IllegalArgumentException::new);


                RecipeIngredient newIngredient = new RecipeIngredient();
                newIngredient.setRecipe(recipe);
                newIngredient.setIngredientManagement(ingredientManage);
                newIngredient.setIngredientIsNecessary(ingredient.isIngredientIsNecessary());

                recipe.getRecipeIngredients().add(newIngredient);
            }
        }

        for (RecipeSource recipeSource : recipeSourcesToDelete) {
            amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, recipeSource.getRecipeSourceServername()));
            recipe.getRecipeSource().remove(recipeSource);
            recipeSourceRepository.delete(recipeSource);
        }

        List<String> existingFileNames = sources.stream()
                .map(RecipeSource::getRecipeSourceFileName)
                .toList();

        List<MultipartFile> filesToAdd = recipeSources.stream()
                .filter(file -> !existingFileNames.contains(file.getOriginalFilename()))
                .toList();

        if(recipeSources != null && !recipeSources.isEmpty()) {
            for(MultipartFile file : recipeSources) {
                RecipeSource recipeSource = RecipeSourceSaveFile(file, recipe, "recipe/");
                recipe.getRecipeSource().add(recipeSource);
            }
        }

        //시간 문제로 레시피 step 전부 삭제 후 생성하는거로 대처
        List<RecipeStep> steps = recipe.getRecipeStep();



        List<RecipeStepUpdateRequest> recipeSteps = request.getRecipeSteps();
        if(recipeSteps != null ) {
            for (int i = 0; i < recipeSteps.size(); i++) {
                RecipeStepUpdateRequest step = recipeSteps.get(i);

                // RecipeStep 저장
                RecipeStep newStep = new RecipeStep();
                newStep.setRecipeStepOrder(i + 1);
                newStep.setRecipeStepContent(step.getRecipeStepContent());
                newStep.setRecipe(recipe);

                if (recipeStepSources != null && recipeStepSources.size() > i) {
                    System.out.println("------------------------------22");
                    MultipartFile file = recipeStepSources.get(i);
                    System.out.println(file);
                    RecipeStepSource stepSource = RecipeStepSourceSaveFile(file, newStep, "recipe/step/");
                    newStep.setRecipeStepSource(stepSource); // 파일 매핑
                }

                recipe.getRecipeStep().add(newStep);
            }
        }

        // 지우는 작업은 늦게함.
        if(steps != null && !steps.isEmpty()) {
            for (RecipeStep step : steps) {
                RecipeStepSource stepSource = step.getRecipeStepSource();
                if(stepSource != null) {
                    amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, "recipe/step/"+stepSource.getRecipeStepSourceServername()));
                }
            }
            //기존 사진 소스 다 삭제 후
            recipe.getRecipeStep().clear();
        }

        recipeRepository.save(recipe);

        return recipe;
    }


    @Override
    public List<RecipeRecommendResponse> getRecommendedRecipes(Long userPk) {
        List<IngredientMyRefrigerator> userIngredients =
                ingredientMyRefrigeratorRepository.findByUserUserPk(userPk);
        List<Recipe> allRecipes = recipeRepository.findAll();

        return allRecipes.stream()
                .map(recipe -> checkRecipeMatch(recipe, userIngredients))
                .filter(RecipeMatchResult::isMatched)
                .map(result -> {
                    RecipeRecommendResponse response = mapper.map(result.getRecipe(), RecipeRecommendResponse.class);
                    response.setMatchRate(result.getMatchRate());
                    response.setRemainExpirationDays(result.getRemainExpirationDays());
                    response.setUrgentIngredientName(result.getUrgentIngredientName());

                    Recipe recipe = result.getRecipe();

                    // 메인 레시피 이미지 설정
                    List<RecipeSourceInfo> mainImages = recipe.getRecipeSource().stream()
                            .filter(source -> source.getRecipeSourceType().getRecipeSourceType().equals("MAIN"))
                            .map(this::convertToRecipeSourceInfo)
                            .collect(Collectors.toList());
                    response.setMainImages(mainImages);

                    // 스텝별 정보와 이미지 설정
                    List<RecipeStepWithImage> steps = recipe.getRecipeStep().stream()
                            .map(step -> {
                                RecipeStepWithImage stepInfo = new RecipeStepWithImage();
                                stepInfo.setOrder(step.getRecipeStepOrder());
                                stepInfo.setContent(step.getRecipeStepContent());

                                // 스텝 이미지 처리
                                if (step.getRecipeStepSource() != null) {
                                    RecipeSourceInfo stepImage = new RecipeSourceInfo();
                                    stepImage.setFilePath(step.getRecipeStepSource().getRecipeStepSourceSave() + "/" +
                                            step.getRecipeStepSource().getRecipeStepSourceServername());
                                    stepImage.setOriginalName(step.getRecipeStepSource().getRecipeStepSourceFileName());
                                    stepInfo.setImages(Collections.singletonList(stepImage));
                                } else {
                                    stepInfo.setImages(Collections.emptyList());
                                }

                                return stepInfo;
                            })
                            .collect(Collectors.toList());
                    response.setSteps(steps);

                    return response;
                })
                .sorted(Comparator.comparingLong(RecipeRecommendResponse::getRemainExpirationDays))
                .collect(Collectors.toList());
    }

    private RecipeSourceInfo convertToRecipeSourceInfo(RecipeSource source) {
        RecipeSourceInfo info = new RecipeSourceInfo();
        info.setFilePath(source.getRecipeSourceSave() + "/" + source.getRecipeSourceServername());
        info.setOriginalName(source.getRecipeSourceFileName());
        return info;
    }

    private RecipeMatchResult checkRecipeMatch(Recipe recipe, List<IngredientMyRefrigerator> userIngredients) {
        List<RecipeIngredient> recipeIngredients = recipeIngredientRepository.findByRecipe(recipe);
        LocalDate currentDate = LocalDate.now();

        if (recipeIngredients.isEmpty()) {
            return new RecipeMatchResult(recipe, false, 0, 0, null);
        }

        boolean hasAllNecessaryIngredients = true;
        int matchedCount = 0;
        long shortestRemainDays = Long.MAX_VALUE;
        String urgentIngredientName = null;

        for (RecipeIngredient recipeIngredient : recipeIngredients) {
            boolean hasIngredient = false;

            for (IngredientMyRefrigerator userIngredient : userIngredients) {
                if (userIngredient.getIngredientManagement().getIngredientManagementPk() ==
                        recipeIngredient.getIngredientManagement().getIngredientManagementPk()) {

                    hasIngredient = true;
                    matchedCount++;

                    // 남은 일수 계산
                    LocalDate expirationDate = LocalDate.parse(userIngredient.getExpirationDate());
                    long remainDays = ChronoUnit.DAYS.between(currentDate, expirationDate);

                    // 더 짧은 유통기한 발견시 정보 업데이트
                    if (remainDays < shortestRemainDays) {
                        shortestRemainDays = remainDays;
                        urgentIngredientName = userIngredient.getIngredientManagement().getIngredientName();
                    }
                    break;
                }
            }

            if (recipeIngredient.isIngredientIsNecessary() && !hasIngredient) {
                hasAllNecessaryIngredients = false;
                break;
            }
        }

        double matchRate = ((double) matchedCount / recipeIngredients.size()) * 100;
        boolean isMatched = hasAllNecessaryIngredients && matchRate >= 66;

        return new RecipeMatchResult(
                recipe,
                isMatched,
                matchRate,
                isMatched ? shortestRemainDays : 0,
                isMatched ? urgentIngredientName : null
        );
    }
    @Override
    public RecipeRecommendResponse getRandomRecipe() {
        List<Recipe> allRecipes = recipeRepository.findAll();

        if (allRecipes.isEmpty()) {
            throw new IllegalStateException("등록된 레시피가 없습니다.");
        }

        Random random = new Random();
        int randomIndex = random.nextInt(allRecipes.size());
        Recipe selectedRecipe = allRecipes.get(randomIndex);

        // Response 객체로 변환
        RecipeRecommendResponse response = mapper.map(selectedRecipe, RecipeRecommendResponse.class);

        // 재료 정보 추가
        List<RecipeIngredient> recipeIngredients = recipeIngredientRepository.findByRecipe(selectedRecipe);
        List<RecipeIngredientInfo> ingredientInfoList = recipeIngredients.stream()
                .map(ri -> {
                    RecipeIngredientInfo info = new RecipeIngredientInfo();
                    info.setIngredientName(ri.getIngredientManagement().getIngredientName());
                    info.setNecessary(ri.isIngredientIsNecessary());
                    return info;
                })
                .collect(Collectors.toList());

        response.setIngredients(ingredientInfoList);

        return response;
    }

    @Override
    @Transactional
    public RecipeLikeResponse toggleLikeDislike(RecipeLikeRequest request) {
        Recipe recipe = recipeRepository.findById(request.getRecipePk())
                .orElseThrow(() -> new IllegalArgumentException("레시피를 찾을 수 없습니다."));

        User user = userRepository.findById(request.getUserPk())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Optional<RecipeLikeDislike> existing =
                recipeLikeDislikeRepository.findByRecipeAndUser(recipe, user);

        if (existing.isPresent()) {
            if (existing.get().getLikeStatus() == request.getLikeStatus()) {
                recipeLikeDislikeRepository.delete(existing.get());
            } else {
                existing.get().setLikeStatus(request.getLikeStatus());
            }
        } else {
            RecipeLikeDislike newReaction = new RecipeLikeDislike();
            newReaction.setRecipe(recipe);
            newReaction.setUser(user);
            newReaction.setLikeStatus(request.getLikeStatus());
            recipeLikeDislikeRepository.save(newReaction);
        }

        // 현재 좋아요/싫어요 수 계산
        long likes = recipeLikeDislikeRepository.countByRecipeAndLikeStatus(recipe, true);
        long dislikes = recipeLikeDislikeRepository.countByRecipeAndLikeStatus(recipe, false);

        return new RecipeLikeResponse(likes, dislikes, request.getLikeStatus());
    }

}