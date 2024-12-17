package moja.refrigerator.service.recipe;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import moja.refrigerator.aggregate.ingredient.IngredientMyRefrigerator;
import moja.refrigerator.aggregate.recipe.*;
import moja.refrigerator.aggregate.user.User;
import moja.refrigerator.dto.recipe.RecipeMatchResult;
import moja.refrigerator.dto.recipe.request.RecipeCreateRequest;
import moja.refrigerator.dto.recipe.request.RecipeLikeRequest;
import moja.refrigerator.dto.recipe.request.RecipeUpdateRequest;
import moja.refrigerator.dto.recipe.response.*;
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

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

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
            RecipeLikeDislikeRepository recipeLikeDislikeRepository
    ) {
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
    }
    private boolean isImageFile(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        return List.of("jpg", "jpeg", "png", "gif").contains(extension);
    }

    private boolean isVideoFile(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        return List.of("mp4", "avi", "mov", "wmv").contains(extension);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void createRecipe(RecipeCreateRequest request, List<MultipartFile> files) {
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

        if(files != null && !files.isEmpty()) {
            for(MultipartFile file : files) {
                try {
                    String recipeSourceFileName = file.getOriginalFilename();
                    UUID uuid = UUID.randomUUID();
                    String recipeSourceServername = uuid + recipeSourceFileName;
                    String recipeSourceSave = "https://" + bucket + "/recipe/" + recipeSourceServername;

                    RecipeSource recipeSource = new RecipeSource();
                    recipeSource.setRecipeSourceServername(recipeSourceServername);
                    recipeSource.setRecipeSourceSave(recipeSourceSave);
                    recipeSource.setRecipeSourceFileName(recipeSourceFileName);

                    RecipeSourceType recipeSourceType;
                    if(isImageFile(recipeSourceFileName)) {
                        recipeSourceType = recipeSourceTypeRepository.findById(1)
                                .orElseThrow(IllegalArgumentException::new);
                    } else if (isVideoFile(recipeSourceFileName)) {
                        recipeSourceType = recipeSourceTypeRepository.findById(2)
                                .orElseThrow(IllegalArgumentException::new);
                    } else {
                        throw new IllegalArgumentException("Unsupported file type");
                    }

                    ObjectMetadata objectMetadata = new ObjectMetadata();
                    objectMetadata.setContentType(file.getContentType());
                    objectMetadata.setContentLength(file.getSize());
                    amazonS3Client.putObject(bucket, recipeSourceServername, file.getInputStream(), objectMetadata);

                    recipeSource.setRecipeSourceType(recipeSourceType);
                    recipeSource.setRecipe(recipe);
                    recipeSourceRepository.save(recipeSource);
                    break;
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        recipeRepository.save(recipe);
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
    public void deleteRecipe(long recipePk) {
        Recipe recipe = recipeRepository.findByRecipePk(recipePk)
                .orElseThrow(IllegalArgumentException::new);
        List<RecipeSource> sources = recipe.getRecipeSource();
        if(sources != null && !sources.isEmpty()) {
            for (RecipeSource recipeSource : sources) {
                amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, recipeSource.getRecipeSourceServername()));
            }
        }
        recipeRepository.delete(recipe);
    }

    @Override
    @Transactional
    public void updateRecipe(RecipeUpdateRequest request, List<MultipartFile> files) {
        Recipe recipe = recipeRepository.findByRecipePk(request.getRecipePk())
                .orElseThrow(() -> new IllegalArgumentException("Recipe not found"));

        if (request.getRecipeName() != null) recipe.setRecipeName(request.getRecipeName());
        if (request.getRecipeCookingTime() != 0) recipe.setRecipeCookingTime(request.getRecipeCookingTime());
        if (request.getRecipeDifficulty() != 0) recipe.setRecipeDifficulty(request.getRecipeDifficulty());
        if (request.getRecipeCategory() != null) {
            recipe.setRecipeCategory(recipeCategoryRepository.findByRecipeCategory(request.getRecipeCategory())
                    .orElseThrow(() -> new IllegalArgumentException("Category not found")));
        }

        List<RecipeSource> sources = recipe.getRecipeSource();
        List<String> uploadedFileNames = files.stream()
                .map(MultipartFile::getOriginalFilename)
                .toList();

        List<RecipeSource> recipeSourcesToDelete = sources.stream()
                .filter(source -> !uploadedFileNames.contains(source.getRecipeSourceFileName()))
                .toList();

        for (RecipeSource recipeSource : recipeSourcesToDelete) {
            amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, recipeSource.getRecipeSourceServername()));
            recipe.getRecipeSource().remove(recipeSource);
            recipeSourceRepository.delete(recipeSource);
        }

        List<String> existingFileNames = sources.stream()
                .map(RecipeSource::getRecipeSourceFileName)
                .toList();

        List<MultipartFile> filesToAdd = files.stream()
                .filter(file -> !existingFileNames.contains(file.getOriginalFilename()))
                .toList();

        for (MultipartFile file : filesToAdd) {
            try {
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

        recipeRepository.save(recipe);
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
                    return response;
                })
                .sorted(Comparator.comparingLong(RecipeRecommendResponse::getRemainExpirationDays))
                .collect(Collectors.toList());
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