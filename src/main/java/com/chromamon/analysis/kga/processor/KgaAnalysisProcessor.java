package com.chromamon.analysis.kga.processor;

import com.chromamon.analysis.kga.model.AnalysisData;
import com.chromamon.analysis.kga.model.DiagnosticData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KgaAnalysisProcessor implements ItemProcessor<AnalysisData, DiagnosticData> {

    @Override
    public DiagnosticData process(AnalysisData data) {
        log.info("Data processing started! - Document ID: {}", data.getDocumentId());
        String result = applyKgaMethod(data);
        log.info("Data process finished! - Document ID: {}", data.getDocumentId());
        return new DiagnosticData(
                data.getTransformerIdentification(),
                "KGA: Key-Gas Analysis",
                result,
                data.getAnalysisTimestamp(),
                data.getDocumentId()
        );
    }

    private String applyKgaMethod(AnalysisData data) {
        double acetylene = data.getC2h2();
        double methane = data.getCh4();
        double ethylene = data.getC2h4();
        double hydrogen = data.getH2();
        double ethane = data.getC2h6();
        double carbonMonoxide = data.getCo();
        double carbonDioxide = data.getCo2();

        double coCo2Ratio = carbonDioxide !=0 ? carbonMonoxide/carbonDioxide : 0;

        StringBuilder diagnostic = new StringBuilder();

        if (acetylene > 50) {
            diagnostic.append("Electrical Arch detected. ");
        }
        if (methane > 40 && ethylene > 30) {
            diagnostic.append("High Intensity thermal fault. ");
        }
        if (hydrogen > 20) {
            diagnostic.append("Partial Discharge detected. ");
        }
        if (ethane > 25) {
            diagnostic.append("Low Intensity thermal fault. ");
        }

        if (coCo2Ratio > 0.1 && coCo2Ratio < 1.0) {
            diagnostic.append("Moderate Degradation of Insulating Paper. ");
        } else if (coCo2Ratio >= 1.0) {
            diagnostic.append("Severe Degradation of Insulating Paper. ");
        }

        if (diagnostic.isEmpty()) {
            diagnostic.append("No faults detected.");
        }

        return diagnostic.toString().trim();
    }
}