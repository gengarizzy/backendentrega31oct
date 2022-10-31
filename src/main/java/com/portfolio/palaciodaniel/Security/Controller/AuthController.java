/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.portfolio.palaciodaniel.Security.Controller;

import com.portfolio.palaciodaniel.Security.DTO.JwtDTO;
import com.portfolio.palaciodaniel.Security.DTO.LoginUsuario;
import com.portfolio.palaciodaniel.Security.Entity.Rol;
import com.portfolio.palaciodaniel.Security.Entity.Usuario;
import com.portfolio.palaciodaniel.Security.Enums.RolNombre;
import com.portfolio.palaciodaniel.Security.Service.RolService;
import com.portfolio.palaciodaniel.Security.Service.UsuarioService;
import com.portfolio.palaciodaniel.Security.jwt.JwtProvider;
import java.util.HashSet;
import java.util.Set;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 Este controlador es el encargado de comunicarse con el Frontend
 */
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "https://secondfrontendupload.web.app") 
public class AuthController {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;
    
    @Autowired
    UsuarioService usuarioService;
    
    @Autowired
    RolService rolService;
    
    @Autowired
    JwtProvider jwtProvider;
    
    @PostMapping("/nuevo")
    public ResponseEntity<?> nuevo(@Valid @RequestBody NuevoUsuario nuevoUsuario, BindingResult bindingResult){
    
        if(bindingResult.hasErrors())
            return new ResponseEntity(new message ("Invalid fields."), HttpStatus.BAD_REQUEST);
        
        if(usuarioService.existsByNombreUsuario(nuevoUsuario.getNombreUsuario()))
            return new ResponseEntity(new message ("Username already exists. Try another one"), HttpStatus.BAD_REQUEST);
        
        if(usuarioService.existsByEmail(nuevoUsuario.getEmail()))
            return new ResponseEntity(new message ("This email is already registered"), HttpStatus.BAD_REQUEST);
        
        Usuario usuario  = new Usuario(nuevoUsuario.getNombre(),
                nuevoUsuario.getNombreUsuario(),
                nuevoUsuario.getEmail(),
                passwordEncoder.encode(nuevoUsuario.getPassword()));
        
        Set<Rol> roles = new HashSet<>();
        roles.add(rolService.getByRolNombre(RolNombre.ROLE_USER).get());
        
       
       
        if(nuevoUsuario.getRoles().contains("admin"))
            roles.add(rolService.getByRolNombre(RolNombre.ROLE_ADMIN).get());
        usuario.setRoles(roles);
        usuarioService.save(usuario);
        
        
      
        usuario.setRoles(roles); 
        usuarioService.save(usuario);
        
   
        return new ResponseEntity(new message ("The account has been saved"), HttpStatus.CREATED);
        
    }
    
    
    @PostMapping("/login")
    public ResponseEntity<JwtDTO> login(@Valid @RequestBody LoginUsuario loginUsuario, BindingResult bindingResult ){
    
        if(bindingResult.hasErrors())
            return new ResponseEntity(new message("Invalid fields"), HttpStatus.BAD_REQUEST);
        
        
        
        
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginUsuario
                        .getNombreUsuario(),loginUsuario.getPassword()));
         
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        String jwt = jwtProvider.generateToken(authentication);
        
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        
        JwtDTO jwtDTO = new JwtDTO(jwt, userDetails.getUsername(), userDetails.getAuthorities());
        
        return new ResponseEntity(jwtDTO, HttpStatus.OK);
        
    }
    
}
