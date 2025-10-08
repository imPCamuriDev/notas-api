package com.pepo.notasapi.Auth.Service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import com.pepo.notasapi.Auth.DTO.AuthResponse;
import com.pepo.notasapi.Auth.DTO.LoginRequest;
import com.pepo.notasapi.Auth.DTO.RegisterRequest;
import com.pepo.notasapi.Security.CustomUserDetailsService;
import com.pepo.notasapi.Security.JWT.JwtUtil;
import com.pepo.notasapi.Usuarios.Usuario;
import com.pepo.notasapi.Usuarios.Repositories.UsuarioRepository;
import com.pepo.notasapi.ValueObjects.EmailVO;
import com.pepo.notasapi.ValueObjects.PasswordHashVO;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;
    private final UsuarioRepository usuarioRepository;

    public AuthService(AuthenticationManager authenticationManager, 
                      JwtUtil jwtUtil,
                      CustomUserDetailsService userDetailsService,
                      UsuarioRepository usuarioRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.usuarioRepository = usuarioRepository;
    }

    public AuthResponse authenticate(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
                )
            );

            Usuario usuario = userDetailsService.loadUserEntityByEmail(loginRequest.getEmail());
            
            String token = jwtUtil.generateToken(
                usuario.getEmail().getValue(),
                usuario.getId()
            );

            return new AuthResponse(
                token,
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail().getValue()
            );

        } catch (AuthenticationException e) {
            throw new RuntimeException("Credenciais inválidas");
        }
    }

    public AuthResponse register(RegisterRequest registerRequest) {
        // Verificar se usuário já existe
        EmailVO emailVO = new EmailVO(registerRequest.getEmail());
        if (usuarioRepository.findByEmail(emailVO).isPresent()) {
            throw new RuntimeException("Email já cadastrado");
        }

        // Criar novo usuário
        Usuario usuario = new Usuario();
        usuario.setNome(registerRequest.getNome());
        usuario.setEmail(emailVO);
        usuario.setPassword(new PasswordHashVO(registerRequest.getPassword()));
        usuario.setEmailVerificado(false);

        usuario = usuarioRepository.save(usuario);

        // Gerar token
        String token = jwtUtil.generateToken(
            usuario.getEmail().getValue(),
            usuario.getId()
        );

        return new AuthResponse(
            token,
            usuario.getId(),
            usuario.getNome(),
            usuario.getEmail().getValue()
        );
    }
}