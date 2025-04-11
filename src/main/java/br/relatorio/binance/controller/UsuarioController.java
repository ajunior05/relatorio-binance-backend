package br.relatorio.binance.controller;

import br.relatorio.binance.configuration.JwtUtil;
import br.relatorio.binance.model.Usuario;
import br.relatorio.binance.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuários", description = "Cadastro e autenticação de usuários")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public UsuarioController(UsuarioService usuarioService, JwtUtil jwtUtil,
                             AuthenticationManager authenticationManager) {
        this.usuarioService = usuarioService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @Operation(summary = "Cadastro de novo usuário", description = "Registra um novo usuário com e-mail e senha.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Email já cadastrado")
    })
    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastrar(@RequestBody Usuario usuario) {
        if (usuarioService.buscarPorEmail(usuario.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email já cadastrado!");
        }
        usuarioService.salvarUsuario(usuario);
        return ResponseEntity.ok("Usuário cadastrado com sucesso!");
    }

    @Operation(summary = "Login de usuário", description = "Autentica o usuário e retorna um token JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login bem-sucedido e token JWT retornado"),
            @ApiResponse(responseCode = "401", description = "Email ou senha inválidos")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario usuario) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(usuario.getEmail(), usuario.getSenha()));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("Email ou senha inválidos.");
        }

        String token = jwtUtil.generateToken(usuario.getEmail());
       // return ResponseEntity.ok(Map.of("token", "Bearer " + token));
        return ResponseEntity.ok(Map.of("token", token));
    }
}