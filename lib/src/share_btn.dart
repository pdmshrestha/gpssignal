import 'package:flutter/material.dart';
import 'package:flutter_speed_dial/flutter_speed_dial.dart';

class ShareBtn extends StatelessWidget {
  final VoidCallback? copy;
  final VoidCallback? share;

  const ShareBtn({Key? key, this.copy, this.share}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return SpeedDial(
      icon: Icons.share,
      activeIcon: Icons.close,
      // backgroundColor: Colors.white,
      // foregroundColor: Colors.black,
      children: [
        SpeedDialChild(
          child: const Icon(Icons.copy),
          label: 'Copy to clipboard',
          labelStyle: const TextStyle(color: Colors.white),
          labelBackgroundColor: Colors.black45,
          onTap: copy,
          backgroundColor: Colors.black45,
          foregroundColor: Colors.white,
        ),
        SpeedDialChild(
          child: const Icon(Icons.map),
          label: 'Share map url',
          labelStyle: const TextStyle(color: Colors.white),
          labelBackgroundColor: Colors.black45,
          onTap: share,
          backgroundColor: Colors.black45,
          foregroundColor: Colors.white,
        ),
      ],
    );
  }
}
