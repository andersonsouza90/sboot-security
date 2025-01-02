package com.dandy.sboot_security.domain.service;

import com.dandy.sboot_security.api.dto.CadastroUsuarioDto;
import com.dandy.sboot_security.domain.entity.Grupo;
import com.dandy.sboot_security.domain.entity.Usuario;
import com.dandy.sboot_security.domain.entity.UsuarioGrupo;
import com.dandy.sboot_security.domain.repository.GrupoRepository;
import com.dandy.sboot_security.domain.repository.UsuarioGrupoRepository;
import com.dandy.sboot_security.domain.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;
    private final GrupoRepository grupoRepository;
    private final UsuarioGrupoRepository usuarioGrupoRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Usuario salvar(CadastroUsuarioDto dto){

        dto.getUsuario().setSenha(passwordEncoder.encode(dto.getUsuario().getSenha()));
        repository.save(dto.getUsuario());

        List<UsuarioGrupo> listaUsuarioGrupo = dto.getPermissoes().stream().map(nomeGrupo ->{
            Optional<Grupo> possivelGrupo = grupoRepository.findByNome(nomeGrupo);
            if (possivelGrupo.isPresent()) {
                Grupo grupo = possivelGrupo.get();
                return new UsuarioGrupo(dto.getUsuario(), grupo);
            }
            return null;
        })
        .filter(grupo -> grupo != null)
        .collect(Collectors.toList());

        usuarioGrupoRepository.saveAll(listaUsuarioGrupo);
        return dto.getUsuario();
    }

    public Usuario obterUsuarioComPermissoes(String login){
        Optional<Usuario> usuarioOptional = repository.findByLogin(login);
        if(usuarioOptional.isEmpty()){
            return null;
        }

        Usuario usuario = usuarioOptional.get();
        List<String> permissoes = usuarioGrupoRepository.findPermissoesByUsuario(usuario);
        usuario.setPermissoes(permissoes);

        return usuario;
    }
}
