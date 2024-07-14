using {com.sap.capjava.handson9999} from '../db/schema';
using {GWSAMPLE_BASIC} from './external/GWSAMPLE_BASIC.csn';

service MyBusinessPartnerService @(requires: 'any') {
    entity BusinessPartnerFromDb as projection on handson9999.BusinessPartner_DB;

    entity BusinessPartnerFromS4 as projection on GWSAMPLE_BASIC.BusinessPartnerSet {
        key BusinessPartnerSet.BusinessPartnerID,
            BusinessPartnerSet.CompanyName,
            BusinessPartnerSet.EmailAddress,
            BusinessPartnerSet.PhoneNumber
    };

    entity BusinessPartner_Combination as
        select from GWSAMPLE_BASIC.BusinessPartnerSet
        inner join handson9999.BusinessPartner_DB
            on BusinessPartnerSet.BusinessPartnerID = BusinessPartner_DB.BusinessPartnerID
        {
            key BusinessPartnerSet.BusinessPartnerID,
                BusinessPartnerSet.CompanyName,
                BusinessPartner_DB.CompanyCode,
                BusinessPartnerSet.EmailAddress,
                BusinessPartnerSet.PhoneNumber,
                BusinessPartner_DB.NumberOfEmployees,
                BusinessPartner_DB.CreatedAt
        }
}
