package br.unitins.service.usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import jakarta.validation.Validator;
import jakarta.ws.rs.NotFoundException;
import br.unitins.dto.endereco.EnderecoDTO;
import br.unitins.dto.usuario.UsuarioDTO;
import br.unitins.dto.usuario.UsuarioResponseDTO;
import br.unitins.model.Endereco;
import br.unitins.model.Sexo;
import br.unitins.model.Telefone;
import br.unitins.model.Usuario;
import br.unitins.repository.CidadeRepository;
import br.unitins.repository.EnderecoRepository;
import br.unitins.repository.TelefoneRepository;
import br.unitins.repository.UsuarioRepository;

@ApplicationScoped
public class UsuarioServicelmpl implements UsuarioService {

    @Inject
    UsuarioRepository usuarioRepository;

    @Inject
    TelefoneRepository telefoneRepository;

    @Inject
    EnderecoRepository enderecoRepository;

    @Inject

    CidadeRepository cidadeRepository;

    @Inject
    Validator validator;

    @Override
    public List<UsuarioResponseDTO> getAll() {
        List<Usuario> list = usuarioRepository.listAll();
        return list.stream().map(UsuarioResponseDTO::new).collect(Collectors.toList());
    }

    @Override
    public UsuarioResponseDTO findById(Long id) {
        Usuario usuario = usuarioRepository.findById(id);
        if (usuario == null)
            throw new NotFoundException("Usuario não encontrado.");
        return new UsuarioResponseDTO(usuario);
    }

    


    // @Override
    // @Transactional
    // public UsuarioResponseDTO create(UsuarioDTO usuarioDTO) throws ConstraintViolationException {
    //     validar(usuarioDTO);

    //     Usuario usuarioBanco = new Usuario();

    //     usuarioBanco.setLogin(usuarioDTO.login());
    //     usuarioBanco.setSenha(usuarioDTO.senha());

    //     usuarioBanco.setEndereco(new ArrayList<>());

    //     for (EnderecoDTO enderecos : usuarioDTO.enderecos()) {
    //         Endereco endereco = new Endereco();
    //         endereco.setBairro(enderecos.bairro());
    //         endereco.setNumero(enderecos.numero());
    //         endereco.setCep(enderecos.cep());
    //         endereco.setComplemento(enderecos.complemento());
    //         endereco.setPrincipal(enderecos.principal());
    //         endereco.setLogradouro(enderecos.logradouro());
    //         endereco.setRua(enderecos.rua());
    //         usuarioBanco.getEndereco().add(endereco);

    //     }

    //     usuarioRepository.persist(usuarioBanco);

    //     return new UsuarioResponseDTO(usuarioBanco);
    // }

    // @Override
    // @Transactional
    // public UsuarioResponseDTO update(Long id, UsuarioDTO usuarioDTO) throws ConstraintViolationException {
    //     validar(usuarioDTO);
    //     Usuario usuarioBanco = usuarioRepository.findById(id);
    //     if (usuarioBanco == null) {
    //         throw new NotFoundException("Usuario não encontrado pelo id");
    //     }

    //     usuarioBanco.setLogin(usuarioDTO.login());
    //     usuarioBanco.setSenha(usuarioDTO.senha());

    //     usuarioBanco.setEndereco(new ArrayList<Endereco>());

    //     for (EnderecoDTO enderecos : usuarioDTO.enderecos()) {
    //         Endereco endereco = new Endereco();
    //         endereco.setBairro(enderecos.bairro());
    //         endereco.setNumero(enderecos.numero());
    //         endereco.setCep(enderecos.cep());
    //         endereco.setComplemento(enderecos.complemento());
    //         endereco.setPrincipal(enderecos.principal());
    //         endereco.setLogradouro(enderecos.logradouro());
    //         endereco.setRua(enderecos.rua());
    //         usuarioBanco.getEndereco().add(endereco);

    //     }

    //     usuarioRepository.persist(usuarioBanco);

    //     return new UsuarioResponseDTO(usuarioBanco);
    // }

    // private void validar(UsuarioDTO UsuarioDTO) throws ConstraintViolationException {
    //     Set<ConstraintViolation<UsuarioDTO>> violations = validator.validate(UsuarioDTO);
    //     if (!violations.isEmpty())
    //         throw new ConstraintViolationException(violations);

    // }

    @Override
    @Transactional
    public void delete(Long id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    public List<UsuarioResponseDTO> findByNome(String nome) {
        List<Usuario> list = usuarioRepository.findByNome(nome);
        return list.stream().map(UsuarioResponseDTO::new).collect(Collectors.toList());
    }

    @Override
    public long count() {
        return usuarioRepository.count();
    }

    @Override
    public Usuario findByLoginAndSenha(String login, String senha) {
        return usuarioRepository.findByLoginAndSenha(login, senha);
    }

    @Override
    public UsuarioResponseDTO findByLogin(String login) {
        Usuario usuario = usuarioRepository.findByLogin(login);
        if (usuario == null)
            throw new NotFoundException("Usuário não encontrado.");
        return new UsuarioResponseDTO(usuario);
    }

    @Override
    @Transactional
    public UsuarioResponseDTO update(Long id, String nomeImagem) {

        Usuario entity = usuarioRepository.findById(id);
        entity.setNomeImagem(nomeImagem);

        return new UsuarioResponseDTO(entity);
    }
}