package com.yunhalee.walkerholic.product.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.persistence.Embeddable;

@Embeddable
public class ReviewInfo {

    private Integer totalCount = 0;
    private Integer totalScore = 0;
    private BigDecimal average;

    public ReviewInfo() {
    }

    public void addReview(Integer rating) {
        totalScore += rating;
        totalCount += 1;
        average = calculateAverage();
    }

    public void editReview(Integer preRating, Integer postRating) {
        totalScore = totalScore - preRating + postRating;
        average = calculateAverage();
    }

    public void deleteReview(Integer rating) {
        totalScore -= rating;
        totalCount -= 1;
        average = calculateAverage();
    }

    private BigDecimal calculateAverage() {
        if (totalCount == 0) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(totalScore).divide(BigDecimal.valueOf(totalCount), 2, RoundingMode.HALF_EVEN);
    }

    public BigDecimal getAverage() {
        return average;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public Integer getTotalScore() {
        return totalScore;
    }
}
