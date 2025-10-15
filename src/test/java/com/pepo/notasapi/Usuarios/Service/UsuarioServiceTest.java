package com.pepo.notasapi.Usuarios.Service;

import com.pepo.notasapi.Usuarios.Usuario;
import com.pepo.notasapi.Usuarios.DTO.UsuarioDTO;
import com.pepo.notasapi.Usuarios.Repositories.UsuarioRepository;
import com.pepo.notasapi.ValueObjects.EmailVO;
import com.pepo.notasapi.ValueObjects.PasswordHashVO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do UsuarioService - TDD")
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuarioValido;
    private EmailVO emailValido;
    private PasswordHashVO senhaValida;

    @BeforeEach
    void setUp() {
        // Given - Prepara dados para todos os testes
        emailValido = new EmailVO("usuario@exemplo.com");
        senhaValida = new PasswordHashVO("Senha@123");
        
        usuarioValido = new Usuario();
        usuarioValido.setId(1L);
        usuarioValido.setNome("João Silva");
        usuarioValido.setEmail(emailValido);
        usuarioValido.setPassword(senhaValida);
        usuarioValido.setEmailVerificado(false);
    }

    // ==================== TESTES DE CRIAÇÃO ====================

    @Test
    @DisplayName("Deve salvar usuário com sucesso")
    void shouldSaveUserSuccessfully() {
        // Given
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioValido);

        // When
        Usuario resultado = usuarioService.salvarUsuario(usuarioValido);

        // Then
        assertNotNull(resultado);
        assertEquals("João Silva", resultado.getNome());
        assertEquals(emailValido, resultado.getEmail());
        assertFalse(resultado.getEmailVerificado());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve gerar ID automaticamente ao salvar usuário")
    void shouldGenerateIdWhenSavingUser() {
        // Given
        Usuario usuarioSemId = new Usuario();
        usuarioSemId.setNome("Maria Santos");
        usuarioSemId.setEmail(new EmailVO("maria@exemplo.com"));
        usuarioSemId.setPassword(new PasswordHashVO("Senha@456"));

        Usuario usuarioComId = new Usuario();
        usuarioComId.setId(2L);
        usuarioComId.setNome("Maria Santos");
        usuarioComId.setEmail(new EmailVO("maria@exemplo.com"));
        usuarioComId.setPassword(new PasswordHashVO("Senha@456"));

        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioComId);

        // When
        Usuario resultado = usuarioService.salvarUsuario(usuarioSemId);

        // Then
        assertNotNull(resultado.getId());
        assertEquals(2L, resultado.getId());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    // ==================== TESTES DE BUSCA ====================

    @Test
    @DisplayName("Deve buscar usuário por ID com sucesso")
    void shouldFindUserByIdSuccessfully() {
        // Given
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioValido));

        // When
        UsuarioDTO resultado = usuarioService.buscarPorId(1L);

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("João Silva", resultado.getNome());
        assertEquals("usuario@exemplo.com", resultado.getEmail());
        verify(usuarioRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar usuário inexistente")
    void shouldThrowExceptionWhenUserNotFound() {
        // Given
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> usuarioService.buscarPorId(999L)
        );

        assertTrue(exception.getMessage().contains("Usuário não encontrado"));
        verify(usuarioRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Deve buscar entidade Usuario por ID")
    void shouldFindUserEntityById() {
        // Given
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioValido));

        // When
        Usuario resultado = usuarioService.buscarUsuarioPorId(1L);

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("João Silva", resultado.getNome());
        assertEquals(emailValido, resultado.getEmail());
        verify(usuarioRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve listar todos os usuários")
    void shouldListAllUsers() {
        // Given
        Usuario usuario2 = new Usuario();
        usuario2.setId(2L);
        usuario2.setNome("Maria Santos");
        usuario2.setEmail(new EmailVO("maria@exemplo.com"));
        usuario2.setPassword(new PasswordHashVO("Senha@456"));

        List<Usuario> usuarios = Arrays.asList(usuarioValido, usuario2);
        when(usuarioRepository.findAll()).thenReturn(usuarios);

        // When
        List<UsuarioDTO> resultado = usuarioService.listarUsuarios();

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("João Silva", resultado.get(0).getNome());
        assertEquals("Maria Santos", resultado.get(1).getNome());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há usuários")
    void shouldReturnEmptyListWhenNoUsers() {
        // Given
        when(usuarioRepository.findAll()).thenReturn(Arrays.asList());

        // When
        List<UsuarioDTO> resultado = usuarioService.listarUsuarios();

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        assertEquals(0, resultado.size());
        verify(usuarioRepository, times(1)).findAll();
    }

    // ==================== TESTES DE DELEÇÃO ====================

    @Test
    @DisplayName("Deve deletar usuário com sucesso")
    void shouldDeleteUserSuccessfully() {
        // Given
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        doNothing().when(usuarioRepository).deleteById(1L);

        // When
        usuarioService.deletarUsuario(1L);

        // Then
        verify(usuarioRepository, times(1)).existsById(1L);
        verify(usuarioRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar usuário inexistente")
    void shouldThrowExceptionWhenDeletingNonExistentUser() {
        // Given
        when(usuarioRepository.existsById(999L)).thenReturn(false);

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> usuarioService.deletarUsuario(999L)
        );

        assertTrue(exception.getMessage().contains("Usuário com ID 999 não encontrado"));
        verify(usuarioRepository, times(1)).existsById(999L);
        verify(usuarioRepository, never()).deleteById(anyLong());
    }

    // ==================== TESTES DE VALIDAÇÃO DE EMAIL ====================

    @Test
    @DisplayName("Deve verificar se email existe")
    void shouldCheckIfEmailExists() {
        // Given
        EmailVO email = new EmailVO("usuario@exemplo.com");
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuarioValido));

        // When
        boolean existe = usuarioService.existsByEmail("usuario@exemplo.com");

        // Then
        assertTrue(existe);
        verify(usuarioRepository, times(1)).findByEmail(any(EmailVO.class));
    }

    @Test
    @DisplayName("Deve retornar false quando email não existe")
    void shouldReturnFalseWhenEmailDoesNotExist() {
        // Given
        EmailVO email = new EmailVO("inexistente@exemplo.com");
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When
        boolean existe = usuarioService.existsByEmail("inexistente@exemplo.com");

        // Then
        assertFalse(existe);
        verify(usuarioRepository, times(1)).findByEmail(any(EmailVO.class));
    }

    // ==================== TESTES DE INTEGRIDADE ====================

    @Test
    @DisplayName("Deve manter emailVerificado como false ao criar novo usuário")
    void shouldKeepEmailVerifiedFalseForNewUser() {
        // Given
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome("Pedro Alves");
        novoUsuario.setEmail(new EmailVO("pedro@exemplo.com"));
        novoUsuario.setPassword(new PasswordHashVO("Senha@789"));

        when(usuarioRepository.save(any(Usuario.class))).thenReturn(novoUsuario);

        // When
        Usuario resultado = usuarioService.salvarUsuario(novoUsuario);

        // Then
        assertNotNull(resultado);
        assertFalse(resultado.getEmailVerificado());
    }

    @Test
    @DisplayName("Deve preservar todos os campos ao salvar usuário")
    void shouldPreserveAllFieldsWhenSavingUser() {
        // Given
        Usuario usuario = new Usuario();
        usuario.setNome("Ana Costa");
        usuario.setEmail(new EmailVO("ana@exemplo.com"));
        usuario.setPassword(new PasswordHashVO("Senha@321"));
        usuario.setEmailVerificado(true);

        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // When
        Usuario resultado = usuarioService.salvarUsuario(usuario);

        // Then
        assertNotNull(resultado);
        assertEquals("Ana Costa", resultado.getNome());
        assertEquals("ana@exemplo.com", resultado.getEmail());
        assertTrue(resultado.getEmailVerificado());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    // ==================== TESTES DE CASOS EXTREMOS ====================

    @Test
    @DisplayName("Deve lidar com múltiplas buscas pelo mesmo ID")
    void shouldHandleMultipleSearchesForSameId() {
        // Given
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioValido));

        // When
        UsuarioDTO resultado1 = usuarioService.buscarPorId(1L);
        UsuarioDTO resultado2 = usuarioService.buscarPorId(1L);

        // Then
        assertNotNull(resultado1);
        assertNotNull(resultado2);
        assertEquals(resultado1.getId(), resultado2.getId());
        verify(usuarioRepository, times(2)).findById(1L);
    }

    @Test
    @DisplayName("Deve verificar se repository é chamado corretamente ao salvar")
    void shouldVerifyRepositoryIsCalledCorrectlyWhenSaving() {
        // Given
        when(usuarioRepository.save(usuarioValido)).thenReturn(usuarioValido);

        // When
        usuarioService.salvarUsuario(usuarioValido);

        // Then
        verify(usuarioRepository).save(argThat(usuario -> 
            usuario.getNome().equals("João Silva") &&
            usuario.getEmail().equals(emailValido)
        ));
    }
}