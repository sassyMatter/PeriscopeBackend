package com.app.services;


import com.app.models.*;
import com.app.models.customModels.*;
import com.app.producers.KafkaProducer;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class CustomCodeService {

    @Autowired
    KafkaProducer kafkaProducer;



























}
