package com.thangnt.ddd.application.service.impl;

import com.thangnt.ddd.application.service.EventAppService;
import org.springframework.stereotype.Service;

@Service
public class EventAppServiceImpl implements EventAppService {
    @Override
    public String showEvent() {
        return "Show event";
    }
}
