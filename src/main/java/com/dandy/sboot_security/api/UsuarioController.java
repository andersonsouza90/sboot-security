package com.dandy.sboot_security.api;

import com.dandy.sboot_security.api.dto.CadastroUsuarioDto;
import com.dandy.sboot_security.domain.entity.Usuario;
import com.dandy.sboot_security.domain.service.UsuarioService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService service;

    @PostMapping
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Usuario> salvar(@RequestBody CadastroUsuarioDto dto){

        Usuario usuarioSalvo = service.salvar(dto);
        return ResponseEntity.ok(usuarioSalvo);
    }
}
