package br.com.icev.dnt.iii.repo;

import br.com.icev.dnt.iii.model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jonny
 */
@Repository
public interface PessoaRepo extends JpaRepository<Pessoa, Integer>{
    
}
