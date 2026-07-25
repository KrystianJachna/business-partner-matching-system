import { useEffect } from "react";
import type { LatLngExpression } from "leaflet";
import {
    MapContainer,
    Marker,
    TileLayer,
    useMap,
    useMapEvents,
} from "react-leaflet";
import "leaflet/dist/leaflet.css";

interface CompanyLocationPickerProps {
    latitude: number | null;
    longitude: number | null;
    onLocationChange: (
        latitude: number,
        longitude: number,
    ) => void;
}

function LocationMarker({
                            latitude,
                            longitude,
                            onLocationChange,
                        }: CompanyLocationPickerProps) {
    useMapEvents({
        click(event) {
            onLocationChange(
                event.latlng.lat,
                event.latlng.lng,
            );
        },
    });

    if (latitude === null || longitude === null) {
        return null;
    }

    return (
        <Marker
            position={[
                latitude,
                longitude,
            ]}
        />
    );
}

interface MapPositionControllerProps {
    latitude: number | null;
    longitude: number | null;
}

function MapPositionController({
                                   latitude,
                                   longitude,
                               }: MapPositionControllerProps) {
    const map = useMap();

    useEffect(() => {
        if (latitude === null || longitude === null) {
            return;
        }

        map.setView(
            [latitude, longitude],
            13,
        );
    }, [
        latitude,
        longitude,
        map,
    ]);

    return null;
}

const DEFAULT_POSITION: LatLngExpression = [
    52.0693,
    19.4803,
];

export function CompanyLocationPicker({
                                          latitude,
                                          longitude,
                                          onLocationChange,
                                      }: CompanyLocationPickerProps) {
    return (
        <MapContainer
            center={DEFAULT_POSITION}
            zoom={6}
            scrollWheelZoom
            style={{
                height: "400px",
                width: "100%",
                borderRadius: "8px",
            }}
        >
            <TileLayer
                attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
            />

            <MapPositionController
                latitude={latitude}
                longitude={longitude}
            />

            <LocationMarker
                latitude={latitude}
                longitude={longitude}
                onLocationChange={onLocationChange}
            />
        </MapContainer>
    );
}
