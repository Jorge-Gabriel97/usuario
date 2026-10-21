package com.jorge.usuario.business.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioDTO {

    private Long id;
    private String nome;
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String senha;
    private List<EnderecoDTO> enderecos;
    private List<TelefoneDTO> telefones;

}
