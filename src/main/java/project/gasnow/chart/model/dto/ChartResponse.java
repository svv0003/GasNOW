package project.gasnow.chart.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ChartResponse {
    private List<Chart> localData;
    private List<Chart> nationwideData;
}