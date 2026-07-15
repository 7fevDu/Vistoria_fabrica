package com.eduardo.formulario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;

@SpringBootApplication
public class FormularioApplication {

    public static void main(String[] args) {
        SpringApplication.run(FormularioApplication.class, args);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> exibirUrl(Environment environment) {
        return event -> {
            String porta = environment.getProperty("local.server.port", "8080");
            System.out.println("Acesse o formulário em: http://localhost:" + porta + "/vistoria");
            String ipRede = descobrirIpRede();
            if (ipRede != null) {
                System.out.println("Acesse pelo celular (mesma rede Wi-Fi) em: http://" + ipRede + ":" + porta + "/vistoria");
            }
        };
    }

    private String descobrirIpRede() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            for (NetworkInterface iface : Collections.list(interfaces)) {
                if (!iface.isUp() || iface.isLoopback() || iface.isVirtual()) {
                    continue;
                }
                Enumeration<InetAddress> enderecos = iface.getInetAddresses();
                for (InetAddress endereco : Collections.list(enderecos)) {
                    if (endereco.isLoopbackAddress() || endereco.getHostAddress().contains(":")) {
                        continue;
                    }
                    return endereco.getHostAddress();
                }
            }
        } catch (SocketException e) {
            return null;
        }
        return null;
    }
}
