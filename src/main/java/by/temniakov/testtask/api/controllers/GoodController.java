package by.temniakov.testtask.api.controllers;

import by.temniakov.testtask.api.dto.InGoodDto;
import by.temniakov.testtask.api.dto.OutGoodDto;
import by.temniakov.testtask.api.services.GoodService;
import by.temniakov.testtask.store.entities.Good;
import by.temniakov.testtask.validation.groups.CreationInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Validated
@RestController
@RequestMapping(path = "/api/goods")
@RequiredArgsConstructor
@Tag(name="good", description = "Good management APIs")
public class GoodController {
    private final GoodService goodService;

    @GetMapping({"{id}"})
    @Operation(
            tags = {"get","good"},
            description = "Get a Good object by specifying its id")
    public ResponseEntity<OutGoodDto> getGood(
            @PathVariable(name = "id") Integer id){
        return ResponseEntity.of(Optional.of(goodService.getDtoByIdOrThrowException(id)));
    }

    @GetMapping()
    @Operation(
            tags = {"get","good"},
            description = "Fetch of good objects by page number and size of this page greater than 0")
    public ResponseEntity<List<OutGoodDto>> fetchGoods(
            @RequestParam(name = "page", defaultValue = "0")
            @Min(value = 0, message = "must be not less than 0") Integer page,
            @RequestParam(name = "size", defaultValue = "50")
            @Min(value = 1,message = "must be not less than 1") Integer size){
        return ResponseEntity.of(Optional.of(goodService.findDtoByPage(page,size)));
    }

    @GetMapping("sort")
    @Operation(
            tags = {"get","good"},
            description = "Fetch of good objects by page number, size of this page greater than 0 " +
                    "and sort fields by \"price\" or \"amount\"")
    public ResponseEntity<List<OutGoodDto>> fetchSortedGoods(
            @PageableDefault(page = 0, size = 50) Pageable pageable){
        return ResponseEntity.of(
                Optional.of(goodService.findSortedDtoByPageable(pageable))
        );
    }

    @PostMapping
    @Operation(
            tags = {"post","good"},
            description = "Create a new good from good object or return already existing good")
    public ResponseEntity<OutGoodDto> createGood(
            @Validated(value = {CreationInfo.class, Default.class})
            @RequestBody InGoodDto createGoodDto){
        OutGoodDto createdGoodDto = goodService
                .getDtoFromGood(goodService.createGood(createGoodDto));

        return ResponseEntity.of(Optional.of(createdGoodDto));
    }

    @PatchMapping("{id}/amount/change")
    @Operation(
            tags = {"patch","good"},
            description = "Update good stock level by difference")
    public ResponseEntity<OutGoodDto> updateGoodAmount(
            @PathVariable(name = "id") Integer id,
            @RequestParam("diff_amount")
            @Parameter(example = "-5")
            Integer diffAmount){
        Good good = goodService.changeStockGoods(id,diffAmount);

        return ResponseEntity.of(
                Optional.of(goodService.getDtoFromGood(good)));
    }

    @DeleteMapping("{id}")
    @Operation(
            tags = {"delete","good"},
            description = "Delete address by its id")
    public ResponseEntity<OutGoodDto> deleteGood(@PathVariable Integer id){
        goodService.delete(id);
        return ResponseEntity.ok().build();
    }
}
