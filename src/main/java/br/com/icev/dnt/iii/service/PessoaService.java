package br.com.icev.dnt.iii.service;

import br.com.icev.dnt.iii.exception.DNTException;
import br.com.icev.dnt.iii.model.Pessoa;
import br.com.icev.dnt.iii.repo.PessoaRepo;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 *
 * @author jonny
 */
@Service
@RequiredArgsConstructor
public class PessoaService {

    private final PessoaRepo repo;

    public List<Pessoa> listPessoas() {
        return repo.findAll();
    }
    public Page<Pessoa> listPessoas(Pessoa pessoa, Pageable pageable) {
        if (Objects.nonNull(pessoa)) {
            Example example = Example.of(pessoa,
                    ExampleMatcher.matching()
                            .withIgnoreCase()
                            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                            .withIgnorePaths("dataCadastro"));
            return repo.findAll(example, pageable);
        }
        return repo.findAll(pageable);
    }

    public Pessoa save(Pessoa pessoa) throws DNTException {
        validate(pessoa);
        return repo.save(pessoa);
    }

    public void patch(Pessoa pessoa) throws DNTException {

        if (Objects.isNull(pessoa)) {
            throw new DNTException("Não foi possível validar a pessoa informada.");
        }

        Optional<Pessoa> optional = repo.findById(pessoa.getId());
        if (optional.isPresent()) {
            Pessoa pessoaBanco = optional.get();
            if (Objects.nonNull(pessoa.getNome())) {
                pessoaBanco.setNome(pessoa.getNome());
            }
            if (Objects.nonNull(pessoa.getCpf())) {
                pessoaBanco.setCpf(pessoa.getCpf());
            }
            if (Objects.nonNull(pessoa.getDataNascimento())) {
                pessoaBanco.setDataNascimento(pessoa.getDataNascimento());
            }
            repo.save(pessoaBanco);
        } else {
            throw new DNTException("Nenhuma pessoa encontrada com o id " + pessoa.getId());
        }
    }

    public Pessoa get(Integer id) throws DNTException {
        return repo.findById(id)
                .orElseThrow(() -> new DNTException("Nenhuma pessoa encontrada com o id " + id));
    }

    public void remove(Integer id) {
        repo.deleteById(id);
    }

    private void validate(Pessoa pessoa) throws DNTException {
        DNTException exception = new DNTException();
        if (Objects.isNull(pessoa)) {
            exception.add("Não foi possível validar os dados da pessoa.");
        } else {
            if (!StringUtils.hasText(pessoa.getNome())) {
                exception.add("Nome da pessoa é obrigatório.");
            }
            if (!StringUtils.hasText(pessoa.getCpf())) {
                exception.add("CPF da pessoa é obrigatório.");
            }
        }
        exception.check();
    }

}
