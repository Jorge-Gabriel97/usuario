package com.jorge.usuario.business;

import com.jorge.usuario.business.converter.UsuarioConverter;
import com.jorge.usuario.business.dto.EnderecoDTO;
import com.jorge.usuario.business.dto.TelefoneDTO;
import com.jorge.usuario.business.dto.UsuarioDTO;
import com.jorge.usuario.infrastructure.entity.Enderecos;
import com.jorge.usuario.infrastructure.entity.Telefone;
import com.jorge.usuario.infrastructure.entity.Usuario;
import com.jorge.usuario.infrastructure.exceptions.ConflictException;
import com.jorge.usuario.infrastructure.exceptions.ResourceNotFoundException;
import com.jorge.usuario.infrastructure.repository.EnderecoRepository;
import com.jorge.usuario.infrastructure.repository.TelefoneRepository;
import com.jorge.usuario.infrastructure.repository.UsuarioRepository;
import com.jorge.usuario.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtil jwtUtil;
    private final EnderecoRepository enderecoRepository;
    private final TelefoneRepository telefoneRepository;

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO) {
        emailExiste(usuarioDTO.getEmail());
        usuarioDTO.setSenha(bCryptPasswordEncoder.encode(usuarioDTO.getSenha()));
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        return usuarioConverter.paraUsuarioDTO(usuarioSalvo);
    }

    public void emailExiste(String email) {
        boolean existe = verificaEmailExistente(email);
        if (existe) {
            throw new ConflictException("Email já cadastrado: " + email);
        }
    }

    public boolean verificaEmailExistente(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public UsuarioDTO buscaUsuarioPorEmail(String email) {
        try {
            return usuarioConverter.paraUsuarioDTO(usuarioRepository.findByEmail(email).orElseThrow(
                    () -> new ResourceNotFoundException("Email não encontrado: " + email)));
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Email não encontrado" + email);
        }
    }

    public void deletarUsuarioPorEmail(String email) {
        usuarioRepository.deleteByEmail(email);
    }

    public UsuarioDTO atualizaDadosUsuario(String token, UsuarioDTO dto) {
        String email = jwtUtil.extrairEmailToken(token.substring(7));

        dto.setSenha(dto.getSenha() != null ? bCryptPasswordEncoder.encode(dto.getSenha()) : null);

        Usuario usuarioEntity = usuarioRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException("Email não localizado"));

        Usuario usuario = usuarioConverter.updateUsuario(dto, usuarioEntity);

        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        return usuarioConverter.paraUsuarioDTO(usuarioSalvo);
    }

    public EnderecoDTO atualizaEndereco(Long idEndereco, EnderecoDTO enderecoDTO) {

        Enderecos entity = enderecoRepository.findById(idEndereco).orElseThrow(() ->
                new ResourceNotFoundException("Id não encontrado: " + idEndereco));

        Enderecos enderecos = usuarioConverter.updateEndereco(enderecoDTO, entity);

        return usuarioConverter.paraEndereco(enderecoRepository.save(enderecos));
    }

    public TelefoneDTO atualizaTelefone(Long idTelefone, TelefoneDTO telefoneDTO) {

        Telefone entity = telefoneRepository.findById(idTelefone).orElseThrow(() ->
                new ResourceNotFoundException("Id não encontrado: " + idTelefone));

        Telefone telefone = usuarioConverter.updateTelefone(telefoneDTO, entity);

        return usuarioConverter.paraTelefone(telefoneRepository.save(telefone));
    }
}