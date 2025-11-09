package com.visuall.controller;

import com.visuall.model.dto.LembreteRequestDTO;
import com.visuall.service.LembreteService;
import com.visuall.util.ErrorResponse;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/lembretes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LembreteController {

    @Inject
    LembreteService lembreteService;

    // ✅ Criar lembrete
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response criarLembrete(LembreteRequestDTO request) {
        try {
            var lembrete = lembreteService.criarLembrete(request);
            return Response.ok(lembrete).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        }
    }

    // 🔧 Editar lembrete existente
    @PUT
    @Path("/{id}")
    public Response editarLembrete(@PathParam("id") Integer id, LembreteRequestDTO request) {
        try {
            var lembreteAtualizado = lembreteService.editarLembrete(id, request);
            return Response.ok(lembreteAtualizado).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        }
    }

    // ☑️ Marcar lembrete como concluído
    @PUT
    @Path("/{id}/concluir")
    public Response concluirLembrete(@PathParam("id") Integer id) {
        try {
            boolean sucesso = lembreteService.marcarComoConcluido(id);
            if (sucesso) {
                return Response.ok().entity("{\"mensagem\": \"Lembrete concluído com sucesso.\"}").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ErrorResponse("Lembrete não encontrado")).build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        }
    }

    // ❌ Excluir lembrete
    @DELETE
    @Path("/{id}")
    public Response excluirLembrete(@PathParam("id") Integer id, @QueryParam("usuarioId") Integer usuarioId) {
        try {
            boolean sucesso = lembreteService.excluirLembrete(id, usuarioId);
            if (sucesso) {
                return Response.ok().entity("{\"mensagem\": \"Lembrete excluído com sucesso.\"}").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ErrorResponse("Lembrete não encontrado ou não pertence ao usuário")).build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        }
    }

    // 🔍 Listar todos os lembretes do usuário
    @GET
    @Path("/usuario/{usuarioId}")
    public Response listarLembretes(@PathParam("usuarioId") Integer usuarioId) {
        try {
            var lembretes = lembreteService.listarPorUsuario(usuarioId);
            return Response.ok(lembretes).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        }
    }

    // 🔍 Listar lembretes ativos (não concluídos)
    @GET
    @Path("/usuario/{usuarioId}/ativos")
    public Response listarLembretesAtivos(@PathParam("usuarioId") Integer usuarioId) {
        try {
            var lembretes = lembreteService.listarAtivos(usuarioId);
            return Response.ok(lembretes).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        }
    }
}
