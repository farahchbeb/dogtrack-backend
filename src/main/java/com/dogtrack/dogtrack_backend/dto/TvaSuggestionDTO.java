package com.dogtrack.dogtrack_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TvaSuggestionDTO {
    @JsonProperty("taux_propose")
    private Double tauxPropose;

    private Double confiance;
    private List<String> justification;
}