import 'package:intl/intl.dart';
import 'package:location/location.dart';

class ListItemModel {
  final String label;
  final String value;

  ListItemModel({required this.label, required this.value});
}

List<ListItemModel> collectListItems(LocationData data) {
  final String lat = data.latitude?.toStringAsPrecision(10) ?? '-';
  final String lon = data.longitude?.toStringAsPrecision(10) ?? '-';
  final String alt = "${data.altitude?.toStringAsPrecision(2) ?? '-'} meter(s)";
  final String acc = "${data.accuracy?.toStringAsPrecision(4) ?? '-'} meter(s)";
  final String time = formatTime(data.time);
  final String speed = formatSpeed(data.speed);
  final String speedAcc = formatSpeed(data.speedAccuracy);

  return [
    ListItemModel(label: "Latitude", value: lat),
    ListItemModel(label: "Longitude", value: lon),
    ListItemModel(label: "Altitude", value: alt),
    ListItemModel(label: "Accuracy", value: acc),
    ListItemModel(label: "Speed", value: speed),
    ListItemModel(label: "Speed accuracy", value: speedAcc),
    ListItemModel(label: "Data received", value: time),
  ];
}

String formatTime(double? milli) {
  if (milli != null) {
    final time = DateTime.fromMillisecondsSinceEpoch(milli.toInt());
    return DateFormat.yMMMMEEEEd().add_Hms().format(time);
  }
  return "-";
}

String formatSpeed(double? speed) {
  if (speed != null) {
    return "${speed * 3.6} km/hr";
  }
  return "-";
}
