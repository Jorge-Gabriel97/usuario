package com.jorge.usuario.business.converter;

import com.jorge.usuario.business.dto.EnderecoDTO;
import com.jorge.usuario.business.dto.TelefoneDTO;
import com.jorge.usuario.business.dto.UsuarioDTO;
import com.jorge.usuario.infrastructure.entity.Enderecos;
import com.jorge.usuario.infrastructure.entity.Telefone;
import com.jorge.usuario.infrastructure.entity.Usuario;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UsuarioConverter {
    public Usuario paraUsuario(UsuarioDTO usuarioDTO) {
        return Usuario.builder()
                .nome(usuarioDTO.getNome())
                .email(usuarioDTO.getEmail())
                .senha(usuarioDTO.getSenha())
                .enderecos(paraListaEnderecosEntity(usuarioDTO.getEnderecos()))
                .telefones(paraListaTelefoneEntity(usuarioDTO.getTelefones()))
                .build();

    }

    public List<Enderecos> paraListaEnderecosEntity(List<EnderecoDTO> enderecoDTOS) {
        return enderecoDTOS.stream().map(this::paraEndereco).toList();
    }

    public Enderecos paraEndereco(EnderecoDTO enderecoDTO) {
        return Enderecos.builder()
                .rua(enderecoDTO.getRua())
                .numero(enderecoDTO.getNumero())
                .cidade(enderecoDTO.getCidade())
                .complemento(enderecoDTO.getComplemento())
                .cep(enderecoDTO.getCep())
                .estado(enderecoDTO.getEstado())
                .build();
    }

    public List<Telefone> paraListaTelefoneEntity(List<TelefoneDTO> telefoneDTOS) {
        return telefoneDTOS.stream().map(this::paraTelefone).toList();
    }

    public Telefone paraTelefone(TelefoneDTO telefoneDTO) {
        return Telefone.builder()
                .numero(telefoneDTO.getNumero())
                .ddd(telefoneDTO.getDdd())
                .build();
    }

    public UsuarioDTO paraUsuarioDTO(Usuario usuario) {
        return UsuarioDTO.builder()
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .senha(usuario.getSenha())
                .enderecos(paraListaEnderecosDTO(usuario.getEnderecos()))
                .telefones(paraListaTelefoneDTO(usuario.getTelefones()))
                .build();
    }

    public List<EnderecoDTO> paraListaEnderecosDTO(List<Enderecos> enderecoDTOS) {
        return enderecoDTOS.stream().map(this::paraEndereco).toList();
    }

    public EnderecoDTO paraEndereco(Enderecos enderecoDTO) {
        return EnderecoDTO.builder()
                .rua(enderecoDTO.getRua())
                .numero(enderecoDTO.getNumero())
                .cidade(enderecoDTO.getCidade())
                .complemento(enderecoDTO.getComplemento())
                .cep(enderecoDTO.getCep())
                .estado(enderecoDTO.getEstado())
                .build();
    }

    public List<TelefoneDTO> paraListaTelefoneDTO(List<Telefone> telefoneDTOS) {
        return telefoneDTOS.stream().map(this::paraTelefone).toList();
    }

    public TelefoneDTO paraTelefone(Telefone telefoneDTO) {
        return TelefoneDTO.builder()
                .numero(telefoneDTO.getNumero())
                .ddd(telefoneDTO.getDdd())
                .build();
    }
}