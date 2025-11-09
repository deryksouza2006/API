package com.visuall.controller;

import com.visuall.service.AuthService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthController {

    AuthService authService = new AuthService();

    @POST
    @Path("/login")
    public Response login(LoginRequest request) {
        try {
            var response = authService.login(request.getEmail(), request.getSenha());
            return Response.ok(response).build();
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        }
    }

    @POST
    @Path("/register")
    public Response register(RegisterRequest request) {
        try {
            var response = authService.register(request.getNome(), request.getEmail(), request.getSenha());
            return Response.ok(response).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        }
    }

    public static class LoginRequest {
        public String email;
        public String senha;

        public String getEmail() { return email; }
        public String getSenha() { return senha; }
    }

    public static class RegisterRequest {
        public String nome;
        public String email;
        public String senha;

        public String getNome() { return nome; }
        public String getEmail() { return email; }
        public String getSenha() { return senha; }
    }

    public static class ErrorResponse {
        public String error;
        public ErrorResponse(String error) { this.error = error; }
    }
}