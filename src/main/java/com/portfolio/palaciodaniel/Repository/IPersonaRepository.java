
package com.portfolio.palaciodaniel.Repository;

import com.portfolio.palaciodaniel.Entity.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IPersonaRepository extends JpaRepository <Persona, Long>{
    
}
