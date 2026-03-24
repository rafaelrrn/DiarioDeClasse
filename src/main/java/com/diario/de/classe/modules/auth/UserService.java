package com.diario.de.classe.modules.auth;

import com.diario.de.classe.modules.auth.dto.RegisterRequest;
import com.diario.de.classe.modules.auth.dto.UserMeResponse;
import com.diario.de.classe.modules.auth.dto.UserUpdateRequest;
import com.diario.de.classe.modules.pessoa.Pessoa;
import com.diario.de.classe.modules.pessoa.PessoaRepository;
import com.diario.de.classe.shared.exception.BusinessException;
import com.diario.de.classe.shared.exception.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PessoaRepository pessoaRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PessoaRepository pessoaRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.pessoaRepository = pessoaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserMeResponse> buscarTodos() {
        return userRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public UserMeResponse buscarPorId(Long id) {
        return toResponse(findOrThrow(id));
    }

    /**
     * Registra um novo usuário, opcionalmente vinculando-o a uma Pessoa existente.
     */
    public UserMeResponse registrar(RegisterRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new BusinessException("Já existe um usuário com o e-mail: " + request.email());
        }

        User user = new User();
        user.setNome(request.nome());
        user.setEmail(request.email());
        user.setSenha(passwordEncoder.encode(request.senha()));
        user.setRole(request.role());
        user.setPessoa(resolvePessoa(request.idPessoa()));

        return toResponse(userRepository.save(user));
    }

    /**
     * Atualiza nome, email, role e vínculo com Pessoa.
     * Se senha for informada, ela é re-codificada. Caso contrário, mantém a atual.
     */
    public UserMeResponse atualizar(Long id, UserUpdateRequest request) {
        User user = findOrThrow(id);

        if (!user.getEmail().equals(request.email()) &&
                userRepository.findByEmail(request.email()).isPresent()) {
            throw new BusinessException("Já existe um usuário com o e-mail: " + request.email());
        }

        user.setNome(request.nome());
        user.setEmail(request.email());
        user.setRole(request.role());
        user.setPessoa(resolvePessoa(request.idPessoa()));

        if (request.senha() != null && !request.senha().isBlank()) {
            user.setSenha(passwordEncoder.encode(request.senha()));
        }

        return toResponse(userRepository.save(user));
    }

    // -------------------------------------------------------------------------

    private User findOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com id: " + id));
    }

    /**
     * Retorna a Pessoa correspondente ao id informado, ou null se id for nulo
     * (desvinculação explícita).
     */
    private Pessoa resolvePessoa(Long idPessoa) {
        if (idPessoa == null) return null;
        return pessoaRepository.findById(idPessoa)
                .orElseThrow(() -> new ResourceNotFoundException("Pessoa não encontrada com id: " + idPessoa));
    }

    private UserMeResponse toResponse(User user) {
        Long idPessoa = user.getPessoa() != null ? user.getPessoa().getIdPessoa() : null;
        return new UserMeResponse(user.getIdUsers(), user.getEmail(), user.getNome(), user.getRole(), idPessoa);
    }
}
