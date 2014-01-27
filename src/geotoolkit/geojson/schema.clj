(ns geotoolkit.geojson.schema
  (:require
    [schema.core :as s] ))


(def Coordinate 
  (s/both
    [s/Num]
    (s/pred #(> (count %) 1) "has at least 2 coordinate numbers")))

(def CoordinateListLevel1 [Coordinate])
(def CoordinateListLevel2 [CoordinateListLevel1])

(def CoordinateList 
  (s/either CoordinateListLevel1 CoordinateListLevel2))

(def Geometry {
  :type (s/both
    s/Str
    (s/pred 
      (fn [x] (some #(= x %) 
        ["Point" "Polygon" "LineString"]))
      "has non-multi geometry type"))
  :coordinates (s/either Coordinate CoordinateList)
})

(def MultiGeometry {
  :type (s/both
    s/Str
    (s/pred 
      (fn [x] (some #(= x %) 
        ["MultiPoint" "MultiPolygon" "MultiLineString" "GeometryCollection"]))
      "has multigeometry type"))
  :geometries [Geometry]
})

(def Feature { 
  :geometry (s/either Geometry MultiGeometry)
  :properties {
    (s/either s/Str s/Keyword) s/Any
  }
  :type (s/both s/Str 
    (s/pred #(= % "Feature") "type of feature is 'Feature'"))
})

(def FeatureCollection {
  :features [Feature]
  :type (s/both s/Str 
    (s/pred #(= % "FeatureCollection") "type of featurecollection is 'FeatureCollection'"))
})
