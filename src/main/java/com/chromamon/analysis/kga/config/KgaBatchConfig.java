package com.chromamon.analysis.kga.config;

import com.chromamon.analysis.kga.model.AnalysisData;
import com.chromamon.analysis.kga.model.DiagnosticData;
import com.chromamon.analysis.kga.repository.DiagnosticRepository;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@AllArgsConstructor
public class KgaBatchConfig {

    private DiagnosticRepository diagnosticRepository;
    private JobRepository jobRepository;
    private PlatformTransactionManager transactionManager;

    @Bean
    public Job kgaAnalysisJob(Step kgaAnalysisStep) {
        return new JobBuilder("kgaAnalysisJob", jobRepository)
                .start(kgaAnalysisStep)
                .build();
    }

    @Bean
    public Step kgaAnalysisStep(ItemReader<AnalysisData> reader,
                                  ItemProcessor<AnalysisData, DiagnosticData> processor,
                                  ItemWriter<DiagnosticData> writer) {
        return new StepBuilder("kgaAnalysisStep", jobRepository)
                .<AnalysisData, DiagnosticData>chunk(10, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}