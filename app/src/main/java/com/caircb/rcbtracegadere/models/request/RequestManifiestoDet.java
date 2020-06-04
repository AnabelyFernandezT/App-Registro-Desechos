package com.caircb.rcbtracegadere.models.request;

import java.math.BigDecimal;
import java.util.List;

public class RequestManifiestoDet {
    private Integer idAppManifiestoDetalle;
    private BigDecimal peso;
    private BigDecimal cantidad;
    private List<RequestManifiestoDetBultos> bultos;

}
