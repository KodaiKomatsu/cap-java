namespace com.sap.capjava.handson9999;

entity BusinessPartner_DB {
    key BusinessPartnerID : String(10);
        CompanyCode       : String(30);
        NumberOfEmployees : Integer;
        CreatedAt         : DateTime;
}
