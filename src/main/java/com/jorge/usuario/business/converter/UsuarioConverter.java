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
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .enderecos(paraListaEnderecosDTO(usuario.getEnderecos()))
                .telefones(paraListaTelefoneDTO(usuario.getTelefones()))
                .build();
    }

    public List<EnderecoDTO> paraListaEnderecosDTO(List<Enderecos> enderecoDTOS) {
        return enderecoDTOS.stream().map(this::paraEndereco).toList();
    }

    public EnderecoDTO paraEndereco(Enderecos endereco) {
        return EnderecoDTO.builder()
                .id(endereco.getId())
                .rua(endereco.getRua())
                .numero(endereco.getNumero())
                .cidade(endereco.getCidade())
                .complemento(endereco.getComplemento())
                .cep(endereco.getCep())
                .estado(endereco.getEstado())
                .build();
    }

    public List<TelefoneDTO> paraListaTelefoneDTO(List<Telefone> telefoneDTOS) {
        return telefoneDTOS.stream().map(this::paraTelefone).toList();
    }

    public TelefoneDTO paraTelefone(Telefone telefone) {
        return TelefoneDTO.builder()
                .id(telefone.getId())
                .numero(telefone.getNumero())
                .ddd(telefone.getDdd())
                .build();
    }

    public Usuario updateUsuario(UsuarioDTO usuarioDTO, Usuario entity) {
        return Usuario.builder()
                .nome(usuarioDTO.getNome() != null ? usuarioDTO.getNome() : entity.getNome())
                .id(entity.getId())
                .senha(usuarioDTO.getSenha() != null ? usuarioDTO.getSenha() : entity.getSenha())
                .email(usuarioDTO.getEmail() != null ? usuarioDTO.getEmail() : entity.getEmail())
                .enderecos(entity.getEnderecos())
                .telefones(entity.getTelefones())
                .build();
    }

    public Enderecos updateEndereco(EnderecoDTO dto, Enderecos entity) {
        return Enderecos.builder()
                .id(entity.getId())
                .rua(dto.getRua() != null ? dto.getRua() : entity.getRua())
                .numero(dto.getNumero() != null ? dto.getNumero() : entity.getNumero())
                .cidade(dto.getCidade() != null ? dto.getCidade() : entity.getCidade())
                .complemento(dto.getComplemento() != null ? dto.getComplemento() : entity.getComplemento())
                .cep(dto.getCep() != null ? dto.getCep() : entity.getCep())
                .estado(dto.getEstado() != null ? dto.getEstado() : entity.getEstado())
                .build();
    }

    public Telefone updateTelefone(TelefoneDTO dto, Telefone entity) {
        return Telefone.builder()
                .id(entity.getId())
                .numero(dto.getNumero() != null ? dto.getNumero() : entity.getNumero())
                .ddd(dto.getDdd() != null ? dto.getDdd() : entity.getDdd())
                .build();
    }
}