package br.com.icev.dnt.iii.controller;

import br.com.icev.dnt.iii.exception.DNTException;
import br.com.icev.dnt.iii.model.Pessoa;
import br.com.icev.dnt.iii.service.PessoaService;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 *
 * @author jonny
 */
@RestController
@RequestMapping("/pessoas")
@RequiredArgsConstructor
public class PessoaController {

    private final PessoaService service;

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<Pessoa> listAll() {
        return service.listPessoas();
    }
    
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<Pessoa> list(
            @RequestBody(required = false) Pessoa pessoa,
            @PageableDefault(sort = "id") Pageable pageable) {
        return service.listPessoas(pessoa, pageable);
    }

    @GetMapping("/param")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity listByParam(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String cpf,
            @RequestParam(required = false) String dataNascimento,
            @PageableDefault(sort = "id") Pageable pageable) {
        Pessoa pessoa = Pessoa.builder()
                .nome(nome)
                .cpf(cpf)
                .build();
        try {
            if (Objects.nonNull(dataNascimento)) {
                pessoa.setDataNascimento(new SimpleDateFormat("dd/MM/yyyy")
                        .parse(dataNascimento));
            }
        } catch (ParseException ex) {
            return ResponseEntity.badRequest()
                    .body("data de nascimento no formato inválido (dd/MM/yyyy)");
        }
        return ResponseEntity.ok(service.listPessoas(pessoa, pageable));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity save(@RequestBody Pessoa pessoa) {
        try {
            Pessoa pessoaSalva = service.save(pessoa);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(pessoaSalva.getId())
                    .toUri();

            return ResponseEntity.created(location).build();
        } catch (DNTException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Pessoa> detail(@PathVariable("id") Integer id) {
        try {
            return ResponseEntity.ok(service.get(id));
        } catch (DNTException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping
    public ResponseEntity<Pessoa> detail(@RequestBody Pessoa pessoa) {
        try {
            service.patch(pessoa);
            return ResponseEntity.accepted().build();
        } catch (DNTException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Integer id) {
        service.remove(id);
    }
}
