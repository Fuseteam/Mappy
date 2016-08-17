package com.test.android.mappy;

import android.content.Context;
import android.database.sqlite.*;


/*
 Class, Created on 10/31/2015, to be used to save data in an SQL database
 makes use of SQLite database
 A single table in a database should be sufficient,
 each record can holds the place name, category, address, latitude, longitude and maybe contact details.
 todo feed information about ATM, BANKS, HOSPITALS, PHARMACIES, SERVICE STATIONS,  into the database in advance.
 todo refer http://www.vogella.com/tutorials/AndroidSQLite/article.html about SQLite and android
 todo refer http://developer.android.com/training/basics/data-storage/databases.html about saving in databases
 */
public class OfflineData extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="MAP_DATA.db";
    private static final int DATABASE_VERSION = 23;
    protected static final String LOCATION_TABLE_NAME = "LocationInformation";
    private static final String COLUMN_ID="_id";
    private static final String COLUMN_PLACE_NAME="Place_Name";
    private static final String COLUMN_CATEGORY ="Category";
    private static final String COLUMN_DISTRICT="District";
    private static final String COLUMN_ADDRESS="Address";
    private static final String COLUMN_LATITUDE="Latitude";
    private static final String COLUMN_LONGITUDE="Longitude";
    protected static final String M_COLUMNS[] = new String[]{COLUMN_PLACE_NAME
            , COLUMN_LATITUDE
            , COLUMN_LONGITUDE
            , COLUMN_ADDRESS
            , COLUMN_CATEGORY
            , COLUMN_DISTRICT};
    private static final String LOCATION_TABLE_CREATE =
            "CREATE TABLE "+LOCATION_TABLE_NAME+
                    "(" +COLUMN_ID+
                    " INTEGER PRIMARY KEY, "+
                    M_COLUMNS[0]+
                    " TEXT, "+
                    M_COLUMNS[4] +
                    " TEXT, " +
                    M_COLUMNS[5] +
                    " TEXT,  "+
                    M_COLUMNS[3] +
                    " TEXT, "+
                    M_COLUMNS[1]+
                    " REAL, "+
                    M_COLUMNS[2]+
                    " REAL);";
    private static final String LOCATION_TABLE_INSERT =
            "INSERT INTO " + LOCATION_TABLE_NAME +
                    " (" + M_COLUMNS[0] + ","
                    + M_COLUMNS[4] + ","
                    + M_COLUMNS[5] + ","
                    + M_COLUMNS[3] + ","
                    + M_COLUMNS[1] + ","
                    + M_COLUMNS[2] + ")" +
                    " Values ";
    private static final String LOCATION_TABLE_RENAME=
            "ALTER TABLE "+LOCATION_TABLE_NAME +
                    " RENAME TO Old_"+LOCATION_TABLE_NAME+";";
    private static final String LOCATION_TABLE_COPY=
            "INSERT INTO "+LOCATION_TABLE_NAME+
                    " SELECT * FROM Old_"+LOCATION_TABLE_NAME+";";
    private static final String OLD_TABLE_DROP=
            "DROP TABLE Old_"+LOCATION_TABLE_NAME+";";
    OfflineData (Context context){//constructor
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(LOCATION_TABLE_CREATE);//creating the table for location information
        db.execSQL(LOCATION_TABLE_INSERT + "('Hakrinbank main branch','Bank','Paramaribo'" +
                ",'Dr. Sophie Redmondstraat 11-13','5.8241373','-55.1613190'), "+//pre-feeding location information
                "('Hakrinbank ATM main branch 1','ATM','Paramaribo'" +
                ",'Dr. Sophie Redmondstraat 11-13','5.8242373','-55.1613190'), "+//pre-feeding location information
                "('Hakrinbank ATM main branch 2','ATM','Paramaribo'" +
                ",'Dr. Sophie Redmondstraat 11-13','5.8241380','-55.1614200'), "+//pre-feeding location information
                "('Hakrinbank ATM main branch 3','ATM','Paramaribo'" +
                ",'Dr. Sophie Redmondstraat 11-13','5.8242370','-55.1614180'), "+//pre-feeding location information
                "('Academisch Ziekenhuis Paramaribo','Hospital','Paramaribo'" +
                ",'Abraham Samsonstraat','5.8381148','-55.1832245'), "+//pre-feeding location information
                "('Hakrinbank ATM AZP','ATM','Paramaribo'" +
                ",'Abraham Samsonstraat','5.8380148','-55.1831245'), "+//pre-feeding location information
                "('Apotheek Academisch Ziekenhuis','Pharmacy','Paramaribo'" +
                ",'Flustraat','5.836843','-55.182106'), "+//pre-feeding location information
                "('Hakrinbank ATM MN','ATM','Paramaribo'" +
                ",'Kwattaweg 319','5.8408915','-55.188083'), "+//pre-feeding location information
                "('Hakrinbank ATM CKC','ATM','Paramaribo'" +
                ",'Waterkant 80','5.8242317','-55.1571087'), "+//pre-feeding location information
                "('Hakrinbank ATM Buitenweg','ATM','Paramaribo'" +
                ",'Sophie Redmondstraat 70','5.8241373','-55.1613190'), "+//pre-feeding location information
                "('Hakrinbank ATM Jong A Kiem','ATM','Paramaribo'" +
                ",'Mr. Jagernath Lachmonstraat 203','5.809010','-55.214102'), " +//pre-feeding location information
                "('Sol Service Station','Service Station','Paramaribo'" +
                ",'Hk. Mr. Jagernath Lachmonstraat/Mottonhoopslaan','5.812945','-55.205129'), "+//pre-feeding location information
                "('Sol Service Station','Service Station','Paramaribo'" +
                ",'van `t Hogerhuysstraat, Beekhuizen','5.810794','-55.170362'), "+//pre-feeding location information
                "('Hakrinbank ATM Charlesburg','ATM','Paramaribo'" +
                ",'Nieuwe Charlesburgweg 139','5.844061','-55.166632'), "+//pre-feeding location information
                "('Hakrinbank ATM Hermitage Mall','ATM','Paramaribo'" +
                ",'Lalla Rookhweg no 70','5.8203033','-55.2048288'), "+//pre-feeding location information
                "('Hakrinbank ATM Zorghotel','ATM','Paramaribo'" +
                ",'Anton Dragtenweg 68','5.833523','-55.132987'), "+//pre-feeding location information
                "('Hakrinbank ATM Wagenwegstraat','ATM','Paramaribo'" +
                ",'Wagenwegstraat','5.829885','-55.159630'), "+//pre-feeding location information
                "('Hakrinbank ATM Weytinghweg','ATM','Paramaribo'" +
                ",'Commisarris Weytinghweg 274','5.81143','-55.25521'), "+//pre-feeding location information
                "('Hakrinbank ATM Wyndham','ATM','Paramaribo'" +
                ",'Domineestraat','5.826847','-55.158069'), "+//pre-feeding location information
                "('Hakrinbank ATM Tulip','ATM','Paramaribo'" +
                ",'Tourtonnelaan 131','5.8361495','-55.1529706'), "+//pre-feeding location information
                "('Hakrinbank ATM Nwe Haven','ATM','Paramaribo'" +
                ",'Havencomplex','5.8110312','-55.1688098'), "+//pre-feeding location information
                "('Hakrinbank ATM Telesur','ATM','Paramaribo'" +
                ",'Zonnebloemstraat','5.821887','-55.185424'), "+//pre-feeding location information
                "('Hakrinbank ATM Flora Branch','ATM','Paramaribo'" +
                ",'Mr. Jagernath Lachmonstraat 164','5.812345','-55.208521'), "+//pre-feeding location information
                "('Hakrinbank Flora Branch','Bank','Paramaribo'" +
                ",'Mr. Jagernath Lachmonstraat 164','5.812453','-55.208421'), "+//pre-feeding location information
                "('Hakrinbank ATM Adek','ATM','Paramaribo'" +
                ",'Leysweg 86','5.812759','-55.215788'), "+//pre-feeding location information
                "('Hakrinbank Tourtonne Branch','Bank','Paramaribo'" +
                ",'Hk. anamoe-/plutostraat','5.8457437','-55.1480277'), "+//pre-feeding location information
                "('Hakrinbank ATM Tourtonne Branch','ATM','Paramaribo'" +
                ",'Hk. anamoe-/plutostraat','5.8456437','-55.1479277'), "+//pre-feeding location information
                "('Hakrinbank ATM RKZ','ATM','Paramaribo'" +
                ",'Koningenstraat 4','5.831839','-55.152106'), "+//pre-feeding location information
                "('S`Lands Hospitaal','Hospital','Paramaribo'" +
                ",'Hk. Tourtonnelaan','5.830451','-55.156558'), "+//pre-feeding location information
                "('Apotheek S`Lands Hospitaal','Pharmacy','Paramaribo'" +
                ",'Henck Arronstraat','5.830551','-55.156658'), "+//pre-feeding location information
                "('Apotheek St. Vincentius Ziekenhuis','Pharmacy','Paramaribo'" +
                ",'Hk. Prince Hendrik-/Koninginnenstraat','5.831625','-55.152756'), "+//pre-feeding location information
                "('St. Vincentius Ziekenhuis','Hospital','Paramaribo'" +
                ",'Hk. Prince Hendrik-/Koninginnenstraat','5.831525','-55.152856'), "+//pre-feeding location information
                "('Hakrinbank ATM Drive Thru','ATM','Paramaribo'" +
                ",'Mr. Jagernath Lachmonstraat 164','5.812354','-55.208321'), "+//pre-feeding location information
                "('Hakrinbank ATM Parima','ATM','Paramaribo'" +
                ",'Mr. Eduard J. Brumastraat 75','5.830203','-55.167038'), "+//pre-feeding location information
                "('Hakrinbank ATM Wang Fu Kwatta','ATM','Paramaribo'" +
                ",'Kwattaweg 599','5.845833','-55.227849'), "+//pre-feeding location information
                "('Hakrinbank ATM Latour Branch','ATM','Paramaribo'" +
                ",'Hk. Indira Gandhi-/Latourweg','5.7892164','-55.1862437');");//pre-feeding location information*/
        db.execSQL(LOCATION_TABLE_INSERT + "('Apotheek Ligeon','Pharmacy','Paramaribo'" +
                ",'Latourweg','5.791231','-55.187408'), "+//pre-feeding location information
                "('Shell Service Station','Service Station','Paramaribo'" +
                ",'Indira Gandhiweg','5.790153','-55.189666'), "+//pre-feeding location information
                "('Hakrinbank ATM King`s Drankenpaleis','ATM','Paramaribo'" +
                ",'Hk. Weg naar Maretraite/Alexander Samuelstraat','5.835834','-55.147837'), "+//pre-feeding location information
                "('Hakrinbank ATM Princess Hotel','ATM','Paramaribo'" +
                ",'Rietbergplein (tegenover Tangelo)','5.827959','-55.148107'), "+//pre-feeding location information
                "('Hakrinbank ATM SOL Jozef Israelstraat','ATM','Paramaribo'" +
                ",'Hk. Jaques Gompertstraat/Jozef Israelstraat','5.8540617','-55.1369530'), "+//pre-feeding location information
                "('SOL Service station','Service Station','Paramaribo'" +
                ",'Hk. Jaques Gompertstraat/Jozef Israelstraat','5.854096','-55.136757'), "+//pre-feeding location information
                "('Hakrinbank ATM Franchepane','ATM','Paramaribo'" +
                ",'Franchepanestraat 121','5.82085','-55.19819'), "+//pre-feeding location information
                "('Hakrinbank ATM Best Mart','ATM','Paramaribo'" +
                ",'Jozef Israelstraat 36','5.8544624','-55.1378026'), "+//pre-feeding location information
                "('Hakrinbank ATM Chi Min','ATM','Paramaribo'" +
                ",'Cornelis Jongbawstraat 83','5.8303216','-55.1429869'), "+//pre-feeding location information
                "('Hakrinbank ATM Nieuw Weer','ATM','Paramaribo'" +
                ",'Hk. Nieuw Weergevondenweg/Magentakanaal','5.794838','-55.230097'), "+//pre-feeding location information
                "('Apotheek Wong','Pharmacy','Paramaribo'" +
                ",'Hk. Prinssessen-/Henck Arronstraat','5.8307295','-55.1585159'), " +//pre-feeding location information
                "('Diakonessen Ziekenhuis','Hospital','Paramaribo'" +
                ",'Hk. Johan Bodegravelaan/Zinniastraat','5.822574','-55.183366'), "+//pre-feeding location information
                "('Apotheek Diakonessenhuis','Pharmacy','Paramaribo'" +
                ",'Hk. Johan Bodegravelaan/Zinniastraat','5.822727','-55.183860'), "+//pre-feeding location information
                "('Hakrinbank ATM filiaal Nickerie','ATM','Nickerie'" +
                ",'Maynardstraat 49','5.947384','-56.993672'), "+//pre-feeding location information
                "('Hakrinbank filiaal Nickerie','Bank','Nickerie'" +
                ",'Maynardstraat 49','5.947284','-56.993572'), "+//pre-feeding location information
                "('Texaco Sevice Station', 'Service Station', 'Marowijne'" +
                ",'Gouverneur van assecklaan','5.618627','-54.398547'), " +//pre-feeding location information
                "('Esso Service Station','Service Station','Marowijne'" +
                ",'Gouverneur van assecklaan','5.618503','-54.396939'), "+//pre-feeding location information
                "('Hakrinbank ATM Sita`s Supermarkt','ATM','Nickerie'" +
                ",'L.H. Wixstraat 5','5.94501','-56.98866'), "+//pre-feeding location information
                "('Apotheek Fung','Pharmacy','Nickerie'" +
                ",'Governeurstraat 104','5.944147','-56.990203'), "+//pre-feeding location information
                "('Streekziekenhuis Nickerie','Hospital','Nickerie'" +
                ",'Annastraat 35','5.940444','-56.986845'), "+//pre-feeding location information
                "('Hakrinbank ATM Esso Bhikie','ATM','Wanica'" +
                ",'Martin Luther King weg 95','5.767904','-55.173206'), "+//pre-feeding location information
                "('Apotheek Tjong A Kiet','Pharmacy','Wanica'" +
                ",'Martin Luther Kingweg 91','5.768016','-55.173281'), "+//pre-feeding location information
                "('SOL service station','Service Station','Wanica'" +
                ",'Martin Luther King weg 95','5.767804','-55.173106'), "+//pre-feeding location information
                "('Hakrinbank ATM Esso Mahesp','ATM','Wanica'" +
                ",'Hk. Indira Gandhiweg/Welgedacht C','5.755794','-55.208577'), "+//pre-feeding information
                "('SOL Service Station','Service Station','Wanica'" +
                ",'Hk. Indira Gandhiweg/Welgedacht C','5.755741','-55.208641'), "+//pre-feeding information
                "('Hakrinbank ATM Dijkveld','ATM','Wanica'" +
                ",'Sir Winston Churchillweg','5.78727','-55.16362'), "+//pre-feeding information
                "('Hakrinbank ATM Jarikaba','ATM','Wanica'" +
                ",'Mr. P. Chandi Shawweg no 102','5.822307','-55.317802'), "+//pre-feeding information
                "('Hakrinbank Tamaredjo Branch','Bank','Commewijne'" +
                ",'Hadji Iding Soemitaweg 471','5.785566','-55.024532'), "+//pre-feeding information
                "('Hakrinbank ATM Tamaredjo Branch','ATM','Commewijne'" +
                ",'Hadji Iding Soemitaweg km 19','5.785666','-55.024632'), " +//pre-feeding information
                "('Shell Service Station','Service Station','Commewijne'" +
                ",'Hadjo Iding Soemitaweg km19','5.785593','-55.024953'), "+//pre-feeding information
                "('Hakrinbank ATM Bridge Mall','ATM','Commewijne'" +
                ",'Meerzorgweg 289','5.8075108','-55.1436003'), "+//pre-feeding location information
                "('Hakrinbank ATM Lelydorp','ATM','Wanica'" +
                ",'Indira Gandhiweg 488','5.699048','-55.216573'), "+//pre-feeding location information
                "('Apotheek Karis','Pharmacy','Wanica'" +
                ",'Indira Gandhiweg','5.750832','-55.210723'), " +//pre-feeding information
                "('Shell Service Station','Service Station','Wanica'" +
                ",'Indira Gandhiweg 488','5.699148','-55.216673'), "+//pre-feeding location information
                "('Hakrinbank ATM Dihal Supermarkt','ATM','Wanica'" +
                ",'Garnizoenspad 84','5.850290','-55.276499'), " +//pre-feeding information
                "('Hakrinbank ATM Warme Bakker Indira Gandhi weg','ATM','Wanica'" +
                ",'Indira Gandhiweg 320','5.757722','-55.207087');");//pre-feeding location information*/
        db.execSQL(LOCATION_TABLE_INSERT+"('Hakrinbank ATM hendrikstraat','ATM','Paramaribo'" +
                ",'Hendrikstraat 182','5.834026','-55.224471'), " +//pre-feeding information
                "('Bhaggoe`s Service Station','Service Station','Paramaribo'" +
                ",'Hendrikstraat 182','5.834053','-55.224456'), "+//pre-feeding location information
                "('Sol Service Station','Service Station','Paramaribo'" +
                ",'Hendrikstraat 103','5.8329824','-55.211982'), "+//pre-feeding location information
                "('Hakrinbank ATM Winkel Mahes','ATM','Nickerie'" +
                ",'Hoofdweg 182, Wageningen','5.766250','-56.687502'), "+//pre-feeding location information
                "('Hakrinbank ATM B & B Trading','ATM','Nickerie'" +
                ",'Corantijnpolder Serie A no 13','5.943016','-57.032672'), "+//pre-feeding location information
                "('Hakrinbank ATM Zhong Zhong Supermarkt','ATM','Nickerie'" +
                ",'Paradise no 15B','5.908632','-56.927340'), "+//pre-feeding location information
                "('DSBank ATM filiaal Nickerie','ATM','Nickerie'" +
                ",'Landingsstraat 6','5.946228','-56.993636'), "+//pre-feeding location information
                "('DSBank filiaal Nickerie','Bank','Nickerie'" +
                ",'Landingsstraat 6','5.946328','-56.993736'), "+//pre-feeding location information
                "('DSBank ATM Ma Retraitemall','ATM','Paramaribo'" +
                ",'Jan Steenstraat 118','5.846186','-55.1357942'), " +//pre-feeding location information
                "('DSBank Ma Retraite','Bank','Paramaribo'" +
                ",'Coronastraat 4','5.840557','-55.1369032'), " +//pre-feeding location information
                "('DSBank ATM Ma Retraite','ATM','Paramaribo'" +
                ",'Coronastraat 4','5.840657','-55.1368032'), " +//pre-feeding location information
                "('DSBank ATM SOL On The Run','ATM','Paramaribo'" +
                ",'Hk. Cornelis Jongbaw=/Wilhelminastraat','5.829835','-55.1436422'), " +//pre-feeding location information
                "('SOL On The Run Service Station','Service Station','Paramaribo'" +
                ",'Hk. Cornelis Jongbaw-/Wilhelminastraat','5.829835','-55.1436422');");//pre-feeding location information*/

    }
    public void onUpgrade(SQLiteDatabase db, int OldVersion,int NewVersion){//whenever the database is updated
        db.execSQL(LOCATION_TABLE_RENAME);//rename the old table
        onCreate(db);//create the new table
        //db.execSQL(LOCATION_TABLE_COPY);//copy data to new table
        db.execSQL(OLD_TABLE_DROP);//delete old table
    }
}