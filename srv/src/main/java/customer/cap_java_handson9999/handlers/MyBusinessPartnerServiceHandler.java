package customer.cap_java_handson9999.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Autowired;

import com.sap.cds.Result;
import com.sap.cds.ql.Select;
import com.sap.cds.services.cds.CdsReadEventContext;

import com.sap.cds.services.cds.CqnService;
import com.sap.cds.services.handler.EventHandler;
import com.sap.cds.services.handler.annotations.After;
import com.sap.cds.services.handler.annotations.On;
import com.sap.cds.services.handler.annotations.ServiceName;
import com.sap.cds.services.persistence.PersistenceService;

import org.springframework.stereotype.Component;

import cds.gen.com.sap.capjava.handson9999.BusinessPartnerDb;
import cds.gen.com.sap.capjava.handson9999.BusinessPartnerDb_;
import cds.gen.gwsample_basic.GwsampleBasic_;
import cds.gen.gwsample_basic.GwsampleBasic;
import cds.gen.mybusinesspartnerservice.BusinessPartnerCombination;
import cds.gen.mybusinesspartnerservice.BusinessPartnerCombination_;
import cds.gen.mybusinesspartnerservice.BusinessPartnerFromDb;
import cds.gen.mybusinesspartnerservice.BusinessPartnerFromDb_;
import cds.gen.mybusinesspartnerservice.BusinessPartnerFromS4_;

@Component
@ServiceName("MyBusinessPartnerService")
public class MyBusinessPartnerServiceHandler implements EventHandler {

    @Autowired
    GwsampleBasic gw;

    private PersistenceService db;

    @Autowired
    public MyBusinessPartnerServiceHandler(PersistenceService db) {
        this.db = db;
    }

    @After(event = CqnService.EVENT_READ, entity = BusinessPartnerFromDb_.CDS_NAME)
    public void readBusinessPartnerFromDb(List<BusinessPartnerFromDb> bpList) {
        bpList.forEach(b -> {
         
            if (b.getCompanyCode() == null) {
                return;
            }
            String companyCode = b.getCompanyCode();
            if (companyCode.length() < 5) {
                b.setCompanyCode(companyCode + " -- might be wrong...");
            }
        });
    }

    @On(entity = BusinessPartnerFromS4_.CDS_NAME)
    public Result readBpMasterS4(CdsReadEventContext context) {
        return gw.run(context.getCqn());
    }

    @On(entity = BusinessPartnerCombination_.CDS_NAME)
    public void readBpMasterFromS4AndHana(CdsReadEventContext context) {
        final List<Map<String, Object>> retList = new ArrayList<>();
        final List<BusinessPartnerCombination> bpCombinationList = new ArrayList<>();
        final Map<String, BusinessPartnerCombination> bpMasterByKeyFromS4 = new HashMap<>();

        // fetch data from S/4HANA
        Select<BusinessPartnerFromS4_> selectS4 = Select.from(BusinessPartnerFromS4_.class).columns(b -> b.BusinessPartnerID(), b -> b.CompanyName(), b -> b.EmailAddress(), b -> b.PhoneNumber());
        Result resultS4 = gw.run(selectS4);

        List<BusinessPartnerCombination> s4List = resultS4.listOf(BusinessPartnerCombination.class);
        s4List.forEach(b -> {
            bpMasterByKeyFromS4.put(b.getBusinessPartnerID(), b);            
        });

        // fetch data from DB
        Select<BusinessPartnerDb_> selectDb = Select.from(BusinessPartnerDb_.class);
        Result resultDb = db.run(selectDb);

        // inner join the data from S/4HANA and from DB
        List<BusinessPartnerDb> dbList = resultDb.listOf(BusinessPartnerDb.class);
        dbList.forEach(d -> {
            if (!bpMasterByKeyFromS4.containsKey(d.getBusinessPartnerID())) return;

            BusinessPartnerCombination bpCombination = bpMasterByKeyFromS4.get(d.getBusinessPartnerID());
            bpCombination.setCompanyCode(d.getCompanyCode());
            bpCombination.setNumberOfEmployees(d.getNumberOfEmployees());
            bpCombination.setCreatedAt(d.getCreatedAt());

            bpCombinationList.add(bpCombination);
        });

        for (final BusinessPartnerCombination b : bpCombinationList) {
            retList.add(b);
        }

        context.setResult(retList);
    }

}
