package com.app.services;


import com.app.models.Response;
import com.app.producers.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomCodeService {

    @Autowired
    KafkaProducer kafkaProducer;







}
