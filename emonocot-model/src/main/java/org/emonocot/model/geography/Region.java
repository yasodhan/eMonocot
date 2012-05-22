package org.emonocot.model.geography;

import org.apache.lucene.spatial.base.context.SpatialContextProvider;
import org.apache.lucene.spatial.base.shape.Shape;

import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

/**
 *
 * @author ben
 *
 */
public enum Region implements GeographicalRegion<Region> {
    NORTHERN_EUROPE(10,"Northern Europe",Continent.EUROPE,1,"POLYGON((-2732031.3718200885 6430998.747479285,3744503.2066853265 6430998.747479285,3744503.2066853265 16048150.791911403,-2732031.3718200885 16048150.791911403,-2732031.3718200885 6430998.747479285))"),
    MIDDLE_EUROPE(11,"Middle Europe",Continent.EUROPE,2,"POLYGON((282934.5848758538 5740106.27876267,2687769.5444829403 5740106.27876267,2687769.5444829403 7372879.214250271,282934.5848758538 7372879.214250271,282934.5848758538 5740106.27876267))"),
    SOUTHWESTERN_EUROPE(12,"Southwestern Europe",Continent.EUROPE,3,"POLYGON((-1056951.3341083059 4287942.907809269,1093808.019712217 4287942.907809269,1093808.019712217 6637425.0195669895,-1056951.3341083059 6637425.0195669895,-1056951.3341083059 4287942.907809269))"),
    SOUTHEASTERN_EUROPE(13,"Southeastern Europe",Continent.EUROPE,4,"POLYGON((736906.6002799034 4137942.3463538173,3303121.4256899995 4137942.3463538173,3303121.4256899995 6150872.08658192,736906.6002799034 6150872.08658192,736906.6002799034 4137942.3463538173))"),
    EASTERN_EUROPE(14,"Eastern Europe",Continent.EUROPE,5,"POLYGON((2184894.8767734086 5524298.243881474,7370524.112243706 5524298.243881474,7370524.112243706 11058147.584800646,2184894.8767734086 11058147.584800646,2184894.8767734086 5524298.243881474))"),
    NORTHERN_AFRICA(20,"Northern Africa",Continent.AFRICA,6,"POLYGON((-1904159.4507395944 2152158.201352498,3987146.300767428 2152158.201352498,3987146.300767428 4514561.993186627,-1904159.4507395944 4514561.993186627,-1904159.4507395944 2152158.201352498))"),
    MACARONESIA(21,"Macaronesia",Continent.AFRICA,7,"POLYGON((-3483181.899845862 1667440.2559119067,-1493935.9952173012 1667440.2559119067,-1493935.9952173012 4796708.134924812,-3483181.899845862 4796708.134924812,-3483181.899845862 1667440.2559119067))"),
    WEST_TROPICAL_AFRICA(22,"West Tropical Africa",Continent.AFRICA,8,"POLYGON((-1952238.3388132073 476054.49268775084,1780738.333499348 476054.49268775084,1780738.333499348 3159813.6488263384,-1952238.3388132073 3159813.6488263384,-1952238.3388132073 476054.49268775084))"),
    WEST_CENTRAL_TROPICAL_AFRICA(23,"West-Central Tropical Africa",Continent.AFRICA,9,"POLYGON((625086.1717780593 -1512117.4525441977,3484605.591530271 -1512117.4525441977,3484605.591530271 1469479.86446741,625086.1717780593 1469479.86446741,625086.1717780593 -1512117.4525441977))"),
    NORTHEAST_TROPICAL_AFRICA(24,"Northeast Tropical Africa",Continent.AFRICA,10,"POLYGON((1498543.4243383997 -186475.0013327255,6064345.735071756 -186475.0013327255,6064345.735071756 2686591.2987201475,1498543.4243383997 2686591.2987201475,1498543.4243383997 -186475.0013327255))"),
    EAST_TROPICAL_AFRICA(25,"East Tropical Africa",Continent.AFRICA,11,"POLYGON((3266207.8825429524 -1316223.267526093,4664859.360716879 -1316223.267526093,4664859.360716879 515134.0521410148,3266207.8825429524 515134.0521410148,3266207.8825429524 -1316223.267526093))"),
    SOUTH_TROPICAL_AFRICA(26,"South Tropical Africa",Continent.AFRICA,12,"POLYGON((1298357.5840448576 -3106028.2523077205,4546960.888017723 -3106028.2523077205,4546960.888017723 -650763.8780972261,1298357.5840448576 -650763.8780972261,1298357.5840448576 -3106028.2523077205))"),
    SOUTHERN_AFRICA(27,"Southern Africa",Continent.AFRICA,13,"POLYGON((1304257.5170569008 -4139717.1890574996,3661347.547062756 -4139717.1890574996,3661347.547062756 -1915330.7743017941,1304257.5170569008 -1915330.7743017941,1304257.5170569008 -4139717.1890574996))"),
    MIDDLE_ATLANTIC_OCEAN(28,"Middle Atlantic Ocean",Continent.AFRICA,14,"POLYGON((-1604799.076098323 -1807420.5971042207,-628426.9542995986 -1807420.5971042207,-628426.9542995986 -880323.9090951623,-1604799.076098323 -880323.9090951623,-1604799.076098323 -1807420.5971042207))"),
    WESTERN_INDIAN_OCEAN(29,"Western Indian Ocean",Continent.AFRICA,15,"POLYGON((4755684.933255112 -2948178.2058719546,8070044.66043956 -2948178.2058719546,8070044.66043956 -476890.5685524602,4755684.933255112 -476890.5685524602,4755684.933255112 -2948178.2058719546))"),
    SIBERIA(30,"Siberia",Continent.ASIA_TEMPERATE,16,"POLYGON((4073240.7949469434 6288022.644271158,18128317.251065798 6288022.644271158,18128317.251065798 16850417.25511303,4073240.7949469434 16850417.25511303,4073240.7949469434 6288022.644271158))"),
    RUSSIAN_FAR_EAST(31,"Russian Far East",Continent.ASIA_TEMPERATE,17,"POLYGON((-20037508.342789244 5204851.357010714,20037502.17791583 5204851.357010714,20037502.17791583 11609805.438812712,-20037508.342789244 11609805.438812712,-20037508.342789244 5204851.357010714))"),
    MIDDLE_ASIA(32,"Middle Asia",Continent.ASIA_TEMPERATE,18,"POLYGON((5176261.101421178 4183155.0221120305,9723550.98083561 4183155.0221120305,9723550.98083561 7448657.108236833,5176261.101421178 7448657.108236833,5176261.101421178 4183155.0221120305))"),
    CAUCASUS(33,"Caucasus",Continent.ASIA_TEMPERATE,19,"POLYGON((4071537.6067378097 4634540.088992676,5607713.183837744 4634540.088992676,5607713.183837744 5919529.975769567,4071537.6067378097 5919529.975769567,4071537.6067378097 4634540.088992676))"),
    WESTERN_ASIA(34,"Western Asia",Continent.ASIA_TEMPERATE,20,"POLYGON((2841668.7405046434 2884997.004200938,8339582.543497311 2884997.004200938,8339582.543497311 5174921.36251785,2841668.7405046434 5174921.36251785,2841668.7405046434 2884997.004200938))"),
    ARABIAN_PENINSULA(35,"Arabian Peninsula",Continent.ASIA_TEMPERATE,21,"POLYGON((3839647.9754663417 1413248.949858324,6662153.664529789 1413248.949858324,6662153.664529789 3783661.5560333948,3839647.9754663417 3783661.5560333948,3839647.9754663417 1413248.949858324))"),
    CHINA(36,"China",Continent.ASIA_TEMPERATE,22,"POLYGON((8195034.184702246 2057328.098907263,15002922.35950292 2057328.098907263,15002922.35950292 7086213.59525613,8195034.184702246 7086213.59525613,8195034.184702246 2057328.098907263))"),
    MONGOLIA(37,"Mongolia",Continent.ASIA_TEMPERATE,23,"POLYGON((9769217.506525652 5098480.9512778325,13351100.233984502 5098480.9512778325,13351100.233984502 6826133.117094639,9769217.506525652 6826133.117094639,9769217.506525652 5098480.9512778325))"),
    EASTERN_ASIA(38,"Eastern Asia",Continent.ASIA_TEMPERATE,24,"POLYGON((13305689.74312328 2502859.5852022846,16232858.017430568 2502859.5852022846,16232858.017430568 5698423.838850938,13305689.74312328 5698423.838850938,13305689.74312328 2502859.5852022846))"),
    INDIAN_SUBCONTINENT(40,"Indian Subcontinent",Continent.ASIA_TROPICAL,25,"POLYGON((6775599.357597217 -76900.7934241193,10840352.640270047 -76900.7934241193,10840352.640270047 4447836.734857816,6775599.357597217 4447836.734857816,6775599.357597217 -76900.7934241193))"),
    INDO_CHINA(41,"Indo-China",Continent.ASIA_TROPICAL,26,"POLYGON((10264207.483720383 627863.3520162682,12897047.02437025 627863.3520162682,12897047.02437025 3318394.1159573356,10264207.483720383 3318394.1159573356,10264207.483720383 627863.3520162682))"),
    MALESIA(42,"Malesia",Continent.ASIA_TROPICAL,27,"POLYGON((10576247.148363004 -1368477.6025675333,15015757.496791393 -1368477.6025675333,15015757.496791393 2405955.01698535,10576247.148363004 2405955.01698535,10576247.148363004 -1368477.6025675333))"),
    PAPUASIA(43,"Papuasia",Continent.ASIA_TROPICAL,28,"POLYGON((14439890.638968702 -1328163.8661899134,18118543.399774145 -1328163.8661899134,18118543.399774145 -967.9056509816368,14439890.638968702 -967.9056509816368,14439890.638968702 -1328163.8661899134))"),
    AUSTRALIA(50,"Australia",Continent.AUSTRALASIA,29,"POLYGON((12568776.977969967 -5411144.574546529,18701679.42034563 -5411144.574546529,18701679.42034563 -1124699.942496685,12568776.977969967 -1124699.942496685,12568776.977969967 -5411144.574546529))"),
    NEW_ZEALAND(51,"New Zealand",Continent.AUSTRALASIA,30,"POLYGON((-19883114.373932384 -6905335.026509339,19935399.940960243 -6905335.026509339,19935399.940960243 -3404071.76868192,-19883114.373932384 -3404071.76868192,-19883114.373932384 -6905335.026509339))"),
    SOUTHWESTERN_PACIFIC(60,"Southwestern Pacific",Continent.PACIFIC,31,"POLYGON((-20037503.375713576 -2595078.3310327,20037508.342789244 -2595078.3310327,20037508.342789244 367156.751113599,-20037503.375713576 367156.751113599,-20037503.375713576 -2595078.3310327))"),
    SOUTH_CENTRAL_PACIFIC(61,"South-Central Pacific",Continent.PACIFIC,32,"POLYGON((-18164252.380491603 -3148731.0816617743,-11739358.913765186 -3148731.0816617743,-11739358.913765186 526671.7285912469,-18164252.380491603 526671.7285912469,-18164252.380491603 -3148731.0816617743))"),
    NORTHWESTERN_PACIFIC(62,"Northwestern Pacific",Continent.PACIFIC,33,"POLYGON((14717365.601720022 586555.3713195858,19157031.79743551 586555.3713195858,19157031.79743551 2792658.570484193,14717365.601720022 2792658.570484193,14717365.601720022 586555.3713195858))"),
    NORTH_CENTRAL_PACIFIC(63,"North-Central Pacific",Continent.PACIFIC,34,"POLYGON((-19747616.289738808 1888744.376533721,-17231461.839338448 1888744.376533721,-17231461.839338448 3276979.8692672295,-19747616.289738808 3276979.8692672295,-19747616.289738808 1888744.376533721))"),
    SUBARCTIC_AMERICA(70,"Subarctic America",Continent.NORTHERN_AMERICA,35,"POLYGON((-19942002.384562027 6658667.767151109,20012733.591214333 6658667.767151109,20012733.591214333 18418391.314130433,-19942002.384562027 18418391.314130433,-19942002.384562027 6658667.767151109))"),
    WESTERN_CANADA(71,"Western Canada",Continent.NORTHERN_AMERICA,36,"POLYGON((-15478725.324849278 6157094.993844011,-9922235.205801183 6157094.993844011,-9922235.205801183 8400607.38101644,-15478725.324849278 8400607.38101644,-15478725.324849278 6157094.993844011))"),
    EASTERN_CANADA(72,"Eastern Canada",Continent.NORTHERN_AMERICA,37,"POLYGON((-10592578.915460119 5112494.948339518,-5857014.381267023 5112494.948339518,-5857014.381267023 8993577.761991771,-10592578.915460119 8993577.761991771,-10592578.915460119 5112494.948339518))"),
    NORTHWESTERN_USA(73,"Northwestern U.S.A.",Continent.NORTHERN_AMERICA,38,"POLYGON((-13883305.51474916 4439023.873941592,-11356007.983320389 4439023.873941592,-11356007.983320389 6275388.290374979,-13883305.51474916 6275388.290374979,-13883305.51474916 4439023.873941592))"),
    NORTH_CENTRAL_USA(74,"North-Central U.S.A.",Continent.NORTHERN_AMERICA,39,"POLYGON((-11581374.292431371 3981528.101835217,-9600466.217663227 3981528.101835217,-9600466.217663227 6339023.789723405,-11581374.292431371 6339023.789723405,-11581374.292431371 3981528.101835217))"),
    NORTHEASTERN_USA(75,"Northeastern U.S.A.",Continent.NORTHERN_AMERICA,40,"POLYGON((-10064735.285965653 4467678.585820028,-7454938.87990999 4467678.585820028,-7454938.87990999 6158952.707439096,-10064735.285965653 6158952.707439096,-10064735.285965653 4467678.585820028))"),
    SOUTHWESTERN_USA(76,"Southwestern U.S.A.",Continent.NORTHERN_AMERICA,41,"POLYGON((-13845545.943472078 3676046.0716132205,-12138962.489865799 3676046.0716132205,-12138962.489865799 5160980.216338894,-13845545.943472078 5160980.216338894,-13845545.943472078 3676046.0716132205))"),
    SOUTH_CENTRAL_USA(77,"South-Central U.S.A.",Continent.NORTHERN_AMERICA,42,"POLYGON((-12139084.941305673 2979204.1417923896,-10412297.00012041 2979204.1417923896,-10412297.00012041 4439107.505879606,-12139084.941305673 4439107.505879606,-12139084.941305673 2979204.1417923896))"),
    SOUTHEASTERN_USA(78,"Southeastern U.S.A.",Continent.NORTHERN_AMERICA,43,"POLYGON((-10532867.140598606 2819141.4817274767,-8353231.510866311 2819141.4817274767,-8353231.510866311 4844778.333534503,-10532867.140598606 4844778.333534503,-10532867.140598606 2819141.4817274767))"),
    MEXICO(79,"Mexico",Continent.NORTHERN_AMERICA,44,"POLYGON((-13180690.284709252 1637451.3103946692,-9651550.731988259 1637451.3103946692,-9651550.731988259 3857986.3288340014,-13180690.284709252 3857986.3288340014,-13180690.284709252 1637451.3103946692))"),
    CENTRAL_AMERICA(80,"Central America",Continent.SOUTHERN_AMERICA,45,"POLYGON((-12160202.248709157 443252.6618111253,-8593681.610979782 443252.6618111253,-8593681.610979782 2094971.5845618783,-12160202.248709157 2094971.5845618783,-12160202.248709157 443252.6618111253))"),
    CARIBBEAN(81,"Caribbean",Continent.SOUTHERN_AMERICA,46,"POLYGON((-9683866.780165546 1123129.6816201382,-6615378.412296199 1123129.6816201382,-6615378.412296199 3813586.2221585717,-9683866.780165546 3813586.2221585717,-9683866.780165546 1123129.6816201382))"),
    NORTHERN_SOUTH_AMERICA(82,"Northern South America",Continent.SOUTHERN_AMERICA,47,"POLYGON((-8168407.760302238 72203.93781244922,-5749412.961466245 72203.93781244922,-5749412.961466245 1368353.4963978226,-8168407.760302238 1368353.4963978226,-8168407.760302238 72203.93781244922))"),
    WESTERN_SOUTH_AMERICA(83,"Western South America",Continent.SOUTHERN_AMERICA,48,"POLYGON((-10203973.70504988 -2620062.1061147703,-6403225.726742379 -2620062.1061147703,-6403225.726742379 1398549.860833391,-10203973.70504988 1398549.860833391,-10203973.70504988 -2620062.1061147703))"),
    BRAZIL(84,"Brazil",Continent.SOUTHERN_AMERICA,49,"POLYGON((-8238817.338228983 -3994464.983307523,-3321779.7701446926 -3994464.983307523,-3321779.7701446926 587919.2294402348,-8238817.338228983 587919.2294402348,-8238817.338228983 -3994464.983307523))"),
    SOUTHERN_SOUTH_AMERICA(85,"Southern South America",Continent.SOUTHERN_AMERICA,50,"POLYGON((-8991804.637852846 -7542445.752255016,-5910291.889560685 -7542445.752255016,-5910291.889560685 -1979724.5321146364,-8991804.637852846 -1979724.5321146364,-8991804.637852846 -7542445.752255016))"),
    SUBANTARCTIC_ISLANDS(90,"Subantarctic Islands",Continent.ANTARCTICA,51,"POLYGON((-6825649.7984556155 -8283351.111124902,17695707.445944976 -8283351.111124902,17695707.445944976 -4446426.419671455,-6825649.7984556155 -4446426.419671455,-6825649.7984556155 -8283351.111124902))"),
    ANTARCTIC_CONTINENT(91,"Antarctic Continent",Continent.ANTARCTICA,52,"POLYGON((-20037508.342789244 -20037508.343038823,20037508.342789244 -20037508.343038823,20037508.342789244 -8512348.512913825,-20037508.342789244 -8512348.512913825,-20037508.342789244 -20037508.343038823))");

    /**
     *
     */
    private Integer code;

    /**
     *
     */
    private String name;

    /**
     *
     */
    private Continent continent;

    /**
    *
    */
    private Integer featureId;

    /**
    *
    */
    private Polygon envelope;

    /**
     * The geographic region as a Shape.
     */
    private Shape shape;

    /**
     *
     * @param newCode Set the code of this region
     * @param newName Set the name of this region
     * @param newContinent Set the continent this region is in
     * @param newFeatureId set the feature id
     * @param newEnvelope set the envelope
     */
    private Region(final Integer newCode, final String newName,
            final Continent newContinent, final Integer newFeatureId,
            final String newEnvelope) {
        this.code = newCode;
        this.name = newName;
        this.continent = newContinent;
        this.featureId = newFeatureId;
        WKTReader wktReader = new WKTReader();
        try {
            this.envelope = (Polygon) wktReader.read(newEnvelope);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param code The code of the region in question
     * @return A Region or throw an IllegalArgumentException if the code does
     *         not match to a region
     */
    public static Region fromString(final String code) {
        int c = Integer.parseInt(code);
        for (Region region : Region.values()) {
            if (c == region.code) {
                return region;
            }
        }
        throw new IllegalArgumentException(code
                + " is not a valid TDWG Level 2 Region Code");
    }

    /**
    *
    * @param code The code of the region in question
    * @return A Region or throw an IllegalArgumentException if the code does
    *         not match to a region
    */
   public static Region fromCode(final int code) {
       for (Region region : Region.values()) {
           if (code == region.code) {
               return region;
           }
       }
       throw new IllegalArgumentException(code
               + " is not a valid TDWG Level 2 Region Code");
   }

    /**
     *
     * @return The code of this region
     */
    public Integer getCode() {
        return code;
    }

    /**
     *
     * @return The name of this region
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return The continent this region is in
     */
    public Continent getContinent() {
        return continent;
    }

    @Override
    public String toString() {
        return Integer.toString(code);
    }

    /**
     *
     * @param other
     *            the other region
     * @return 1 if other is after this, -1 if other is before this and 0 if
     *         other is equal to this
     */
    public int compareNames(final Region other) {
        return this.name.compareTo(other.name);
    }

    /**
     * @return the featureId
     */
    public Integer getFeatureId() {
        return featureId;
    }

    /**
     * @return the envelope
     */
    public Polygon getEnvelope() {
        return envelope;
    }

    /**
     * @return the shape
     */
    public final Shape getShape() {
        return shape;
    }

    /**
     * @param newShape Set the shape
     */
    public final void setShape(final Shape newShape) {
        this.shape = newShape;
    }
}
