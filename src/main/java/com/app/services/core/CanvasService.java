package com.app.services.core;

import com.app.models.canvas.CanvasData;
import com.app.repository.CanvasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CanvasService implements com.app.services.interfaces.CanvasService {

    @Autowired
    CanvasRepository canvasRepository;

    public CanvasData saveCanvasData(CanvasData canvasData) {
        return canvasRepository.save(canvasData);
    }

    public List<CanvasData> getAllCanvasData() {
        return canvasRepository.findAll();
    }

}
