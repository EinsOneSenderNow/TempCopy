package ru.itis.javalab.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.itis.javalab.dto.ads.AdDto;
import ru.itis.javalab.dto.ads.AdsPage;
import ru.itis.javalab.dto.ads.NewAdDto;
import ru.itis.javalab.dto.ads.UpdateAdDto;
import ru.itis.javalab.exceptions.NotFoundException;
import ru.itis.javalab.models.Ad;
import ru.itis.javalab.repositories.AdsRepository;
import ru.itis.javalab.repositories.UsersRepository;
import ru.itis.javalab.services.AdsService;

import static ru.itis.javalab.dto.ads.AdDto.from;

@Service
@RequiredArgsConstructor
public class AdsServiceImpl implements AdsService {

    private final AdsRepository adsRepository;
    private final UsersRepository usersRepository;

    @Value("${default.page-size}")
    private int defaultPageSize;

    @Override
    public void deleteAd(Long id) {
        Ad adForDelete = getAdOrThrow(id);

        adForDelete.setState(Ad.State.DELETED);

        adsRepository.save(adForDelete);
    }

    @Override
    public AdsPage getAllAds(int page) {
        PageRequest pageRequest = PageRequest.of(page, defaultPageSize, Sort.Direction.ASC, "id");
        Page<Ad> adsPage = adsRepository.findAllByStateOrderById(pageRequest, Ad.State.ACTIVE);
        return AdsPage.builder()
                .ads(from(adsPage.getContent()))
                .totalPagesCount(adsPage.getTotalPages())
                .build();
    }

    @Override
    public AdDto updateAd(Long id, UpdateAdDto updateAdDto) {
        Ad ad = getAdOrThrow(id);

        ad.setDescription(updateAdDto.getDescription());
        ad.setHeader(updateAdDto.getHeader());
        ad.setPrice(updateAdDto.getPrice());
        ad.setAddress(updateAdDto.getAddress());

        adsRepository.save(ad);
        return from(ad);
    }

    @Override
    public AdDto addAd(NewAdDto newAdDto) {
        Ad ad = Ad.builder()
                .user(usersRepository.findById(newAdDto.getUserId())
                        .orElseThrow(() -> new NotFoundException("No user with id: <" + newAdDto.getUserId() + "> found")))
                .header(newAdDto.getHeader())
                .description(newAdDto.getDescription())
                .state(Ad.State.DRAFT)
                .price(newAdDto.getPrice())
                .address(newAdDto.getAddress())
                .build();

        adsRepository.save(ad);

        return from(ad);
    }

    @Override
    public AdDto getAd(Long id) {
        Ad ad = getAdOrThrow(id);
        return from(ad);
    }


    @Override
    public AdDto acceptAd(Long id) {
        Ad adForAccept = getAdOrThrow(id);

        adForAccept.setState(Ad.State.ACCEPTED);
        adsRepository.save(adForAccept);

        return from(adForAccept);
    }

    @Override
    public AdDto publishAd(Long id) {
        Ad adForPublish = getAdOrThrow(id);

        adForPublish.setState(Ad.State.ACTIVE);

        adsRepository.save(adForPublish);

        return from(adForPublish);
    }

    @Override
    public AdDto blockAd(Long id) {
        Ad adForBlock = getAdOrThrow(id);

        adForBlock.setState(Ad.State.BLOCKED);
        adsRepository.save(adForBlock);

        return from(adForBlock);
    }

    private Ad getAdOrThrow(Long id) {

        Ad ad = adsRepository.findById(id).orElseThrow(() -> new NotFoundException("Объявление не найдено"));
        if (ad.getState() == Ad.State.DELETED) {
            throw new NotFoundException("Объявление не найдено");
        }

        return ad;
    }
}
