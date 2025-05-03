package com.example.usuarios.service;

import com.example.usuarios.dto.UsuarioRequest;
import com.example.usuarios.dto.UsuarioResponse;
import com.example.usuarios.exception.ResourceNotFoundException;
import com.example.usuarios.exception.UserAlreadyExistsException;
import com.example.usuarios.model.Usuario;
import com.example.usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService implements UserDetailsService {
    
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));
    }
    
    public UsuarioResponse crearUsuario(UsuarioRequest usuarioRequest) {
        if (usuarioRepository.existsByEmail(usuarioRequest.getEmail())) {
            throw new UserAlreadyExistsException("El email ya está en uso: " + usuarioRequest.getEmail());
        }
        
        Usuario usuario = Usuario.builder()
                .nombre(usuarioRequest.getNombre())
                .apellido(usuarioRequest.getApellido())
                .email(usuarioRequest.getEmail())
                .password(passwordEncoder.encode(usuarioRequest.getPassword()))
                .roles(usuarioRequest.getRoles())
                .tipo(Usuario.TipoUsuario.valueOf(usuarioRequest.getTipo()))
                .build();
        
        Usuario guardado = usuarioRepository.save(usuario);
        
        return mapToDto(guardado);
    }
    
    public List<UsuarioResponse> obtenerTodos() {
        return usuarioRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
    
    public UsuarioResponse obtenerPorId(String id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
        
        return mapToDto(usuario);
    }
    
    public UsuarioResponse actualizarUsuario(String id, UsuarioRequest usuarioRequest) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
        
        usuario.setNombre(usuarioRequest.getNombre());
        usuario.setApellido(usuarioRequest.getApellido());
        
        // Verificamos si se intenta cambiar el email a uno que ya existe
        if (!usuario.getEmail().equals(usuarioRequest.getEmail()) && 
                usuarioRepository.existsByEmail(usuarioRequest.getEmail())) {
            throw new UserAlreadyExistsException("El email ya está en uso: " + usuarioRequest.getEmail());
        }
        
        usuario.setEmail(usuarioRequest.getEmail());
        
        // Solo actualizamos la contraseña si se proporciona una nueva
        if (usuarioRequest.getPassword() != null && !usuarioRequest.getPassword().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(usuarioRequest.getPassword()));
        }
        
        usuario.setRoles(usuarioRequest.getRoles());
        usuario.setTipo(Usuario.TipoUsuario.valueOf(usuarioRequest.getTipo()));
        
        Usuario actualizado = usuarioRepository.save(usuario);
        
        return mapToDto(actualizado);
    }
    
    public void eliminarUsuario(String id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario no encontrado con id: " + id);
        }
        
        usuarioRepository.deleteById(id);
    }
    
    public UsuarioResponse buscarPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + email));
        
        return mapToDto(usuario);
    }
    
    private UsuarioResponse mapToDto(Usuario usuario) {
        return UsuarioResponse.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .email(usuario.getEmail())
                .roles(usuario.getRoles())
                .tipo(usuario.getTipo())
                .build();
    }
}