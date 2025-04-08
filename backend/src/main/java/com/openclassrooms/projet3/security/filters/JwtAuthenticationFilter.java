package com.openclassrooms.projet3.security.filters;

import com.openclassrooms.projet3.security.JwtProvider;
import com.openclassrooms.projet3.service.impl.UserServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtProvider jwtProvider; // Service pour gérer les JWT
    @Autowired
    private UserServiceImpl userServiceImpl; // Service pour récupérer les informations de l'utilisateur

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // Récupère le header Authorization de la requête
        String authHeader = request.getHeader("Authorization");

        // Vérifie si le header est nul ou ne commence pas par "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response); // Passe au filtre suivant sans authentification
            return;
        }

        // Extrait le token JWT en supprimant "Bearer " (les 7 premiers caractères)
        String jwt = authHeader.substring(7);
        // Extrait le nom d'utilisateur du JWT
        String email = jwtProvider.extractUsername(jwt);

        // Vérifie que le nom d'utilisateur est valide et que l'utilisateur n'est pas déjà authentifié
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Charge l'utilisateur depuis la base de données
            UserDetails userDetails = userServiceImpl.loadUserByEmail(email);

            // Vérifie si le token est valide
            if (jwtProvider.validateToken(jwt, userDetails)) {
                // Crée un objet d'authentification avec les informations de l'utilisateur
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                // Ajoute l'utilisateur authentifié au contexte de sécurité
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        // Continue l'exécution des autres filtres
        chain.doFilter(request, response);
    }
}


