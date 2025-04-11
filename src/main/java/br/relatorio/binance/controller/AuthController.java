package br.relatorio.binance.controller;
import br.relatorio.binance.DTO.UsuarioCadastroDTO;
import br.relatorio.binance.model.Usuario;
import br.relatorio.binance.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Endpoints para registro e autenticação de usuários")
@SecurityRequirement(name = "Bearer JWT")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/register")
    @Operation(summary = "Registrar novo usuário", description = "Cria um novo usuário com email e senha.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário registrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "E-mail já cadastrado ou dados inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno ao registrar usuário")
    })
    public ResponseEntity<?> cadastrar(@RequestBody @Valid UsuarioCadastroDTO usuarioDTO) {

        if (usuarioRepository.findByEmail(usuarioDTO.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("E-mail já cadastrado.");
        }

        Usuario novoUsuario = new Usuario();
        novoUsuario.setEmail(usuarioDTO.getEmail());
        novoUsuario.setSenha(usuarioDTO.getSenha()); // depois podemos criptografar

        usuarioRepository.save(novoUsuario);

        return ResponseEntity.ok("Usuário cadastrado com sucesso!");
    }
}