package com.dandy.sboot_security.api;

import com.dandy.sboot_security.api.dto.CadastroUsuarioDto;
import com.dandy.sboot_security.domain.entity.Usuario;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rh")
public class RhController {

    @GetMapping("/tecnico")
    @PreAuthorize("hasAnyRole('TECNICO_RH', 'GERENTE_RH','ADMIN')")
    public ResponseEntity<String> tecnico(){
        return ResponseEntity.ok("Rota do técnico");
    }

    @GetMapping("/gerente")
    @PreAuthorize("hasAnyRole('GERENTE_RH','ADMIN')")
    public ResponseEntity<String> gerente(){
        return ResponseEntity.ok("Rota do gerente");
    }
}