package com.jorge.usuario.business;

import com.jorge.usuario.business.converter.UsuarioConverter;
import com.jorge.usuario.business.dto.UsuarioDTO;
import com.jorge.usuario.infrastructure.entity.Usuario;
import com.jorge.usuario.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        return usuarioConverter.paraUsuario(usuarioRepository.save(usuario));
    }
}
