package com.dogtrack.dogtrack_backend.dto;

public record LoginResponse(String token, String login, String nom, String role) {}