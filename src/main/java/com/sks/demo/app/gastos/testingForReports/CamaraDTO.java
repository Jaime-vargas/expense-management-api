package com.sks.demo.app.gastos.testingForReports;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class CamaraDTO {
        private String id;
        private String serie;
        private String modelo;
        private String patchPanel;

        private String muto;
        private String switchPort;
        private String ubicacion;
        private String idf;

        private String vistaHacia;   // ruta o base64
        private String vistaDesde;

        private String usuario;
        private String password;

        private String servidor;
}

