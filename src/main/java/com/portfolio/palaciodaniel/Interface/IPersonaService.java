
package com.portfolio.palaciodaniel.Interface;
import com.portfolio.palaciodaniel.Entity.Persona;
import java.util.List;



public interface IPersonaService {
    //Importar una lista de personas, con ESTE METODO
    public List<Persona> getPersona();
    
    //Guardar un objeto de tipo Persona
    public void savePersona(Persona persona);
            
    //Eliminar un usuario por ID
    public void deletePersona(Long id);
    
    //Buscar una persona por ID
    public Persona findPersona(Long id);
}
