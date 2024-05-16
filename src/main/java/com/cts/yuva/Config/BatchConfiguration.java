package com.cts.yuva.Config;
import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.Transactional;

import com.cts.yuva.Entity.Customer;
import com.cts.yuva.Repo.CustomerRepository;

import lombok.AllArgsConstructor;

@Configuration
@EnableBatchProcessing
@AllArgsConstructor
public class BatchConfiguration {

    private JobBuilderFactory jobBuilderFactory;

    
    private StepBuilderFactory stepBuilderFactory;

  
    private CustomerRepository customerRepository;


    @Bean
    public FlatFileItemReader<Customer> reader() {
        FlatFileItemReader<Customer> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("customers.csv"));
        reader.setLinesToSkip(1);

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setDelimiter(",");
        tokenizer.setStrict(false);
        tokenizer.setNames("id", "firstName", "lastName", "email");

        DefaultLineMapper<Customer> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(new BeanWrapperFieldSetMapper<Customer>() {{
            setTargetType(Customer.class);
        }});

        reader.setLineMapper(lineMapper);
        return reader;
    }

    @Bean
    public ItemWriter<Customer> writer() {
        return items -> {
            customerRepository.saveAll(items);
            
            customerRepository.flush();
        };
    }

    @Bean
    public  CustomItemProcessor processor() {
    	return new CustomItemProcessor();
    }
    @Bean
    @Transactional
    public Step step1(ItemReader<Customer> reader, ItemWriter<Customer> writer) {
        return stepBuilderFactory.get("step1")
                .<Customer, Customer>chunk(8)
                .reader(reader)
                .processor(processor())
                .writer(writer)
                .build();
    }

    @Bean
    public Job importJob(Step step1) {
        return jobBuilderFactory.get("importJob")
                .flow(step1)
                .end()
                .build();
    }
}
