package by.temniakov.testtask.api.controllers;

import by.temniakov.testtask.api.dto.InGoodDto;
import by.temniakov.testtask.api.dto.OutGoodDto;
import by.temniakov.testtask.api.services.GoodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Validated
@Transactional
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name="good", description = "Good management APIs")
public class GoodController {
    private final GoodService goodService;

    public static final String GET_GOOD = "/goods/{id_good}";
    public static final String FETCH_GOODS = "/goods";
    public static final String FETCH_SORTED_GOODS = "/goods/sort";
    public static final String CREATE_GOOD  = "/goods";
    public static final String DELETE_GOOD  = "/goods/{id_good}";
    public static final String UPDATE_GOOD = "/goods/{id_good}";

    @GetMapping(GET_GOOD)
    @Operation(
            tags = {"get","good"},
            summary = "Retrieve good by Id",
            description = "Get a Good object by specifying its id")
    public ResponseEntity<OutGoodDto> getGood(
            @Parameter(description = "Id of retrieving good", example = "1")
            @PathVariable(name = "id_good") Integer goodId){
        return ResponseEntity.of(Optional.of(goodService.getDtoByIdOrThrowException(goodId)));
    }

    @GetMapping(FETCH_GOODS)
    @Operation(
            tags = {"get","good"},
            summary = "Retrieve goods by page and size",
            description = "Fetch of good objects by page number and size of this page greater than 0")
    public ResponseEntity<List<OutGoodDto>> fetchGoods(
            @Parameter(description = "Number of searching page", example = "0")
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @Parameter(description = "Size of searching page", example = "50")
            @RequestParam(name = "size", defaultValue = "50") Integer size){
        return ResponseEntity.of(Optional.of(goodService.findDtoByPage(page,size)));
    }

    @GetMapping(FETCH_SORTED_GOODS)
    @Operation(
            tags = {"get","good"},
            summary = "Retrieve goods by page, size and sort",
            description = "Fetch of good objects by page number, size of this page greater than 0 " +
                    "and sort fields")
    public ResponseEntity<List<OutGoodDto>> fetchSortedGoods(Pageable pageable){

        return ResponseEntity.of(
                Optional.of(goodService.findSortedDtoByPageable(pageable))
        );
    }

    @PostMapping(CREATE_GOOD)
    @Operation(
            tags = {"post","good"},
            summary = "Create a new good",
            description = "Create a new good from good object or return already existing good")
    public ResponseEntity<OutGoodDto> createGood(
            @RequestBody InGoodDto createGoodDto){
        OutGoodDto createdGoodDto = goodService
                .getDtoFromGood(goodService.createGood(createGoodDto));

        return ResponseEntity.of(Optional.of(createdGoodDto));
    }

    @PatchMapping(value = UPDATE_GOOD)
    @Operation(
            tags = {"patch","good"},
            summary = "Update existing good by id",
            description = "Update good by its id and good object or return already existing good with different id")
    public ResponseEntity<OutGoodDto> updateGood(
            @Parameter(description = "Id of updated good", example = "1")
            @PathVariable(name = "id_good") Integer goodId,
            @RequestBody InGoodDto goodDto){
        OutGoodDto updatedGoodDto = goodService
                .getDtoFromGood(goodService.getUpdatedOrExistingGood(goodId, goodDto));

        return ResponseEntity.of(
                Optional.of(updatedGoodDto));
    }

    @DeleteMapping(DELETE_GOOD)
    @Operation(
            tags = {"delete","good"},
            summary = "Remove good by Id",
            description = "Delete address by its id")
    public ResponseEntity<OutGoodDto> deleteGood(
            @Parameter(description = "Good id to be deleted", example = "1")
            @PathVariable(name = "id_good") Integer goodId){
        goodService.delete(goodId);

        return ResponseEntity.ok().build();
    }
}
