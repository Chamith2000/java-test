package com.newsportal.service;

import com.newsportal.dto.RegisterRequest;
import com.newsportal.entity.User;

public interface UserService {
    User registerNewReader(RegisterRequest request);
}
