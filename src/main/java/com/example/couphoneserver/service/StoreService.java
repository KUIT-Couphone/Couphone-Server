package com.example.couphoneserver.service;

import com.example.couphoneserver.common.datatype.Coordinate;
import com.example.couphoneserver.common.exception.BrandException;
import com.example.couphoneserver.common.exception.StoreException;
import com.example.couphoneserver.domain.entity.Brand;
import com.example.couphoneserver.domain.entity.Store;
import com.example.couphoneserver.dto.store.GetNearbyStoreResponse;
import com.example.couphoneserver.dto.store.LocationInfo;
import com.example.couphoneserver.dto.store.PostStoreRequest;
import com.example.couphoneserver.dto.store.PostStoreResponse;
import com.example.couphoneserver.repository.BrandRepository;
import com.example.couphoneserver.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.*;

import static com.example.couphoneserver.common.response.status.BaseExceptionResponseStatus.BRAND_NOT_FOUND;
import static com.example.couphoneserver.common.response.status.BaseExceptionResponseStatus.DUPLICATE_STORE_NAME;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final BrandRepository brandRepository;
    private final MemberService memberService;
    /*
    가게 등록
     */
    @Transactional
    public PostStoreResponse save(PostStoreRequest request) {
        //매장 브랜드 찾기
        Brand brandOfStore = validateBrand(request.getBid());
        //지점명 중복 확인
        validateStoreName(request);
        //매장 등록
        Store store = storeRepository.save(request.toEntity(brandOfStore));
        return new PostStoreResponse(store.getId());
    }

    /*
    가게 조회
     */
    public List<GetNearbyStoreResponse> findNearbyStores(Principal principal, LocationInfo request, String query){
//        translateEPSG5181(request);
        if(query == null) query = "%%";
        else query = "%"+query+"%";
        Long memberId = findMemberIdByPrincipal(principal);
        List<GetNearbyStoreResponse> storeList =new ArrayList<>(getCandidateStoreList(request, memberId, query));
        storeList.sort((o1, o2) -> {
            int o1Stamp = o1.getGetBrandResponse().getStampCount();
            int o2Stamp = o2.getGetBrandResponse().getStampCount();

            if (o1Stamp < o2Stamp) return 1;
            if (o1Stamp == o2Stamp && o1.getDistance() > o2.getDistance()) return 1;
            return -1;
        });
        return storeList;
    }

//    private void translateEPSG5181(LocationInfo request) {
//        String address = coordinateConverter.getAddress(request.getLongitude(), request.getLatitude());
//        Coordinate coordinate = coordinateConverter.getCoordinate(address);
//        request.setLongitude(coordinate.getLongitude());
//        request.setLatitude(coordinate.getLatitude());
//    }

    private Set<GetNearbyStoreResponse> getCandidateStoreList(LocationInfo request, Long memberId, String query) {
        request.setDistance();
        double x = request.getLongitude();
        double y = request.getLatitude();
        double radius = request.getDistance();
        double minLongitude = x - radius;
        double maxLongitude = x + radius;
        double minLatitude = y - radius;
        double maxLatitude = y + radius;
        Set<GetNearbyStoreResponse> storeList = new LinkedHashSet<>();
        storeRepository.findNearbyStores(memberId,minLongitude,maxLongitude,minLatitude,maxLatitude,query).stream().forEach(c -> {
            GetNearbyStoreResponse response = c.translateResponse();
            Coordinate coordinate = c.translateCoordinate();
            response.setDistance(calculateDistance(x,y,coordinate));
            storeList.add(response);
        });

        if(storeList.size() < 10){
            log.info("additional");
            List<GetNearbyStoreResponse> tempList = new LinkedList<>();
            storeRepository.findNearbyAdditional(minLongitude,maxLongitude,minLatitude,maxLatitude,query).stream().forEach(c -> {
                GetNearbyStoreResponse response = c.translateResponse();
                Coordinate coordinate = c.translateCoordinate();
                response.setDistance(calculateDistance(x,y,coordinate));
                tempList.add(response);
            });
            tempList.sort((o1, o2) -> o1.getDistance() > o2.getDistance() ? 1 : -1);
            storeList.addAll(tempList);
        }
        return storeList;
    }

    private Long findMemberIdByPrincipal(Principal principal) {
        String email = principal.getName();
        return memberService.findOneByEmail(email).getId();
    }

    private double calculateDistance(double x, double y, Coordinate coordinate) {
        double distanceX = Math.abs(coordinate.getLongitude() - x);
        double distanceY = Math.abs(coordinate.getLatitude() - y);
        return Math.sqrt(distanceX*distanceX+distanceY*distanceY);
    }

    private void validateStoreName(PostStoreRequest postStoreRequest) {
//        log.info("[StoreService.validateStoreName]");
        if(storeRepository.existsByName(postStoreRequest.getName()))
            throw new StoreException(DUPLICATE_STORE_NAME);
    }

    private Brand validateBrand(Long brandId) {
//        log.info("[StoreService.validateBrand]");
        Optional<Brand> brand = brandRepository.findById(brandId);
        if(brand.isEmpty()) throw new BrandException(BRAND_NOT_FOUND);
        return brand.get();
    }

}
