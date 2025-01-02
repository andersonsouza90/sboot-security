package com.dandy.sboot_security.api.dto;

import com.dandy.sboot_security.domain.entity.Usuario;
import lombok.Data;

import java.util.List;

@Data
public class CadastroUsuarioDto {
    private Usuario usuario;
    private List<String> permissoes;
}
