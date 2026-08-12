# Product Requirements Document (PRD)
## SmartScan — Document Scanner, Converter & Sharing App

**Version:** 1.0
**Platform:** Android (Kotlin, Jetpack Compose)
**Build tool target:** Google Antigravity (agentic IDE)
**Primary Goal:** Scan physical documents with the phone camera, auto-enhance them, convert them into multiple digital formats, and share them via WhatsApp, email, cloud storage, and other apps.

> **Note for the build agent (Antigravity):** This PRD is written to be actionable. Section 7 gives the exact tech stack and architecture to scaffold the project. Section 9 gives the MVP scope as a literal build checklist — build in that order. Do not implement Phase 2/3 features until the MVP checklist is fully complete and passes the acceptance criteria in Section 10.

---

## 1. Product Overview

SmartScan is an Android app that captures physical documents via the camera and converts them into high-quality digital documents. It should automatically detect document boundaries, correct perspective, enhance image quality, run OCR, and export to:

- PDF (standard, high-quality, compressed, searchable)
- JPG / PNG
- Microsoft Word (.docx)
- Plain text (.txt)

Documents can be organized locally and shared via WhatsApp, Email, Google Drive, OneDrive, Bluetooth, Nearby Share, and any other installed app — using Android's native Share Sheet, not custom per-app integrations.

**Core product loop:** `Scan → Auto Detect → Enhance → OCR → Export → Share`

---

## 2. Problem Statement

Users need to digitize IDs, invoices, receipts, bills, certificates, contracts, notes, forms, books, and office documents, but traditional scanning requires either a physical scanner or clunky manual camera + crop + convert workflows.

| Today | SmartScan |
|---|---|
| Camera → Photograph → Crop manually → Edit → Convert → Save → Find file → Share | Scan → Auto Detect → Enhance → OCR → Export → Share |

**North-star UX goal:** A user should be able to scan a document and send it on WhatsApp in under one minute.

---

## 3. Target Users & Use Cases

| Segment | Key use cases |
|---|---|
| Students | Scan notes/textbooks, convert to PDF, extract text, submit assignments |
| Office employees | Scan invoices/forms, digitize documents, convert to Word, email documents |
| Business owners | Invoice/receipt scanning, archiving, sharing with employees/customers |
| Government/admin users | Application forms, certificates, ID documents, official correspondence |
| General users | Bills, receipts, IDs, agreements, personal documents |

---

## 4. Core User Flow

```
Open App → Tap "Scan" → Camera Opens → Document Detection → Capture
→ Auto Crop → Perspective Correction → Image Enhancement → OCR Processing
→ Preview → Edit → Select Output Format → Save → Share
```

---

## 5. Screens & Functional Requirements

### 5.1 Splash Screen
Logo, app name ("SmartScan"), tagline ("Scan. Convert. Share."), loading animation.

### 5.2 Onboarding (first-time users, 4 screens, Skip/Next/Get Started)
1. Scan Any Document — capture with your phone camera
2. Automatic Enhancement — auto crop, straighten, enhance
3. Convert to Multiple Formats — PDF, Word, JPG, PNG, Text
4. Share Anywhere — WhatsApp, Email, Drive, and other apps

### 5.3 Home Dashboard
- Header: logo/name, search, settings
- Primary CTA: large **"+ Scan Document"** button (must always be the most visually prominent element)
- Quick actions: Scan, Import Image, Import PDF, OCR, Create PDF
- Recent Documents list: thumbnail, name, page count, date, file type, overflow menu

### 5.4 Document Scanner (Camera Screen)
Camera preview, flash toggle, gallery import, auto-capture, manual capture, camera switch, live document-edge detection overlay, zoom, grid, capture button.

### 5.5 Automatic Document Detection
Detects the four corners of the document in the live camera frame and highlights the boundary in real time.

### 5.6 Automatic Perspective Correction
Transforms an angled capture into a clean rectangular document (warp/deskew).

### 5.7 Image Enhancement
Filters: Original, Auto, Document, Black & White, Grayscale, Color, Low Light, High Contrast.
Manual adjustments: Brightness, Contrast, Sharpness, Saturation, Exposure.

### 5.8 Manual Crop
Drag corners, rotate, crop, reset.

### 5.9 Multi-Page Scanning
Continuous multi-page capture. Actions: add page, delete page, duplicate page, reorder page (drag-and-drop), rotate page.

### 5.10 Document Preview
Thumbnail strip of all pages + "Add Page" + Edit/Save actions.

### 5.11 OCR (Optical Character Recognition)
Extract printed text, multi-paragraph text, numbers, tables (best-effort), headings.
- Initial languages: English, Hindi, Gujarati
- Future languages: Marathi, Bengali, Tamil, Telugu, Kannada, Malayalam, Punjabi, Urdu, others

### 5.12 OCR Text Editor
View/edit extracted text: select, copy, edit, delete, search, replace, select all, share text.

### 5.13 Export Formats
- **PDF:** standard, high-quality, compressed, searchable (OCR-backed)
- **Word (.docx):** OCR text converted into editable Word content, preserving headings/paragraphs/line breaks/tables/basic formatting where feasible (perfect layout reconstruction is NOT guaranteed for arbitrary documents — treat as advanced/best-effort)
- **Images:** JPG, PNG
- **Text:** TXT
- **Future:** Excel, CSV, HTML, RTF

### 5.14 PDF Tools
Create (image→PDF, multi-image→PDF, OCR→searchable PDF), edit (add/delete/reorder/rotate pages), compress (High/Medium/Small with estimated output size shown before export), and future security (password protection, encryption, permission control).

### 5.15 Document Naming
Auto-generated default name, e.g. `Scan_2026_08_13_001.pdf`, user-renamable (e.g. "Electricity Bill August 2026").

### 5.16 Document Library
Categories: All Documents, PDFs, Word, Images, Recent, Favorites.
Actions: open, rename, delete, share, export, duplicate, move, favorite.

### 5.17 Search
Search by filename (MVP), and OCR text/date/file type (Phase 2).

### 5.18 Folders (Phase 2)
Create/rename/delete folders; move documents between them.

### 5.19 Sharing
Use Android's native Share Sheet exclusively — no bespoke per-app SDKs. Must work with WhatsApp, Gmail, Outlook, Google Drive, OneDrive, Bluetooth, Nearby Share, Telegram, and any other installed app. WhatsApp sharing requires no WhatsApp credentials from the app.

### 5.20 Import
Import existing JPG/PNG/PDF from Gallery, Files, Camera, or other document providers via the Android file picker. For PDFs: preview pages, reorder, delete, add scanned pages, extract pages, merge PDFs (Phase 2 for merge/split).

### 5.21 Settings
Scanner (auto capture, auto crop, default filter/resolution), Export (default format/quality/compression), OCR (default language, auto-OCR), Storage (default location, auto-cleanup), Sharing (default format), Security (app lock, biometric), Cloud (backup, sync), Appearance (light/dark/system).

### 5.22 App Lock (Phase 2)
Use Android's supported BiometricPrompt API (fingerprint/face/device credential) — do not build a custom biometric mechanism.

### 5.23 Notifications
Minimal, dismissible: "Document processing complete", "Cloud backup completed." User can disable non-essential notifications.

### 5.24 Error Handling
Every error must show an actionable message:
| Failure | Message |
|---|---|
| Camera failure | "Unable to access camera." |
| Poor image | "Document is too blurry. Please scan again." |
| OCR failure | "Unable to recognize text." |
| Export failure | "Unable to create document." |
| Storage failure | "Not enough storage available." |
| Sharing failure | "Unable to share this document." |

---

## 6. Non-Functional Requirements

### 6.1 Offline-First
Core scanning must work fully offline: camera capture, image processing, cropping, perspective correction, filters, on-device OCR, PDF generation, JPG/PNG export, Word generation, local storage, and sharing. Internet is never mandatory for basic scanning — important for sensitive documents.

### 6.2 Privacy & Security
- Process documents locally wherever possible; never upload without explicit consent
- Encrypt sensitive local data where appropriate
- Secure any cloud uploads (encrypted in transit)
- Provide document delete functionality
- Request only necessary permissions (Camera; Photos/Media on import; Notifications only if used) — never contacts, SMS, microphone, or location
- Default posture: **local-first, privacy-first**

### 6.3 Performance
- Fast camera open, responsive preview
- All image processing, OCR, and PDF/DOCX generation must run asynchronously off the main thread (background workers)
- Multi-page/large documents processed in batches; never load all full-resolution pages into memory at once
- UI must never freeze during OCR or PDF generation

### 6.4 Accessibility
Android font scaling, screen reader support, high-contrast mode, large touch targets, content descriptions, accessible buttons, dark mode.

### 6.5 Reliability
Survive: lifecycle changes, screen rotation, background/foreground transitions, low-memory devices, very long PDFs, storage limits, interrupted processing, app restarts, permission denial/revocation, corrupt images/PDFs. **A document must never be lost because the user backgrounds the app.**

### 6.6 Analytics & Crash Monitoring
Privacy-conscious analytics only (app opened, scan started/completed, export type, share initiated, export failed). **Never** collect document contents, OCR text, or file contents in analytics or crash reports.

---

## 7. Technical Architecture (for Antigravity to scaffold)

### 7.1 Stack
- **Language:** Kotlin
- **UI:** Jetpack Compose
- **Architecture:** Clean Architecture + MVVM (`UI → ViewModel → Use Cases → Repositories → Data Sources`)
- **Camera:** CameraX (preview, image capture, image analysis, auto-focus, flash, camera switch)
- **OCR:** Google ML Kit Text Recognition (on-device), abstracted behind an interface so another engine can be swapped in later
- **Local DB:** Room
- **PDF/DOCX generation:** dedicated internal services (see below)

### 7.2 Module Layout
```
SmartScan
├── presentation/ (home, scanner, preview, editor, documents, settings, tools)
├── domain/ (model, repository, usecase)
├── data/ (local, database, filesystem, preferences)
├── scanner/ (camera, detection, crop, enhancement)
├── ocr/ (engine, processor, languages)
├── document/ (pdf, word, image, text)
└── sharing/ (AndroidShare)
```

### 7.3 Document Detection Pipeline
```
Camera Frame → Image Analysis → Edge/ML Detection → Document Boundary
→ Corner Detection → Perspective Transformation
```

### 7.4 OCR Pipeline
```
Image → Preprocessing → OCR Engine (ML Kit) → Recognized Text
→ Text Processing → Word / Searchable PDF
```

### 7.5 PDF Service Interface
```kotlin
interface PdfService {
    fun createPdf(pages: List<Page>): File
    fun mergePdf(files: List<File>): File
    fun splitPdf(file: File, range: IntRange): File
    fun compressPdf(file: File, level: CompressionLevel): File
    fun rotatePages(file: File, pageIndices: List<Int>, degrees: Int): File
    fun deletePages(file: File, pageIndices: List<Int>): File
    fun reorderPages(file: File, newOrder: List<Int>): File
    fun createSearchablePdf(pages: List<Page>, ocrText: List<String>): File
}
```

### 7.6 Word Generation Pipeline
```
Scanned Document → OCR → Structured Text → Formatting Detection → DOCX Generator → Document.docx
```

### 7.7 Room Schema
```
Document: id, name, createdAt, updatedAt, pageCount, fileType, filePath, folderId, ocrText, isFavorite, fileSize
DocumentPage: id, documentId, pageNumber, imagePath, rotation, ocrText
Folder: id, name, createdAt
```

### 7.8 File Storage Layout
```
documents/{yyyy}/{mm}/document_{id}/
    page_001.jpg
    page_002.jpg
    document.pdf
    document.docx
```
Temporary processing files are deleted automatically after a successful export.

### 7.9 Permissions
Request only when needed: Camera (scanning), Photos/Media (import, per Android version's media permission model), Notifications (only if background-processing notifications are added later).

---

## 8. Navigation

Bottom navigation: **Home | Documents | Scan | Tools | More**

Tools section: Image→PDF, PDF→Word, PDF→JPG, JPG→PDF, OCR, Merge PDF, Split PDF, Compress PDF, Rotate PDF, Extract Pages.

---

## 9. MVP Scope — Build Checklist (build in this exact order)

**Phase 1 — Foundation:** Android project → Jetpack Compose → Navigation → Room Database → File Storage

**Phase 2 — Scanner:** CameraX → Image Capture → Document Detection → Crop → Perspective Correction → Enhancement

**Phase 3 — Documents:** Multi-page → Preview → Reorder → Delete → Rename

**Phase 4 — Conversion:** PDF → OCR → TXT → DOCX

**Phase 5 — Sharing:** Android Share Sheet → WhatsApp → Email → Drive

**Phase 6 — Production:** Security → Performance → Crash Handling → Testing → Play Store release prep

### MVP feature checklist
- [ ] Camera scanning
- [ ] Automatic document detection
- [ ] Manual crop
- [ ] Perspective correction
- [ ] Image enhancement (filters)
- [ ] Multi-page scanning
- [ ] Page reorder/delete
- [ ] PDF generation
- [ ] JPG/PNG export
- [ ] OCR (English, Hindi, Gujarati)
- [ ] TXT export
- [ ] Basic Word (.docx) export
- [ ] Local document library
- [ ] Search by filename
- [ ] Rename / delete documents
- [ ] Android Share Sheet integration
- [ ] WhatsApp sharing (via Share Sheet)
- [ ] Email sharing (via Share Sheet)
- [ ] Dark mode
- [ ] Basic settings
- [ ] No account/login required

### Explicitly out of MVP scope
Folders, favorites, PDF merge/split/compression, password-protected PDF, app lock/biometrics, cloud backup/Drive/OneDrive sync, OCR-text search, AI features (classification, naming, summarization, Q&A), digital signatures, ID/passport/business-card scanning — all deferred to Phase 2/3 (see Section 11).

---

## 10. Acceptance Criteria

**Scanning:** camera opens; document captured; boundaries auto-detected; manual boundary correction works; perspective correction works; enhancement filters apply correctly.

**Multi-page:** user can scan multiple pages, reorder them, delete them, add more.

**PDF:** PDF generates successfully; opens correctly in standard PDF readers; multi-page order is preserved.

**OCR:** text extracts correctly; user can view and copy extracted text.

**Word:** OCR text exports to a valid .docx that opens in Word-compatible apps.

**Sharing:** Share Sheet opens; WhatsApp and email apps can receive the generated file.

**Storage:** documents persist across app restarts; rename/delete work correctly.

---

## 11. Post-MVP Roadmap

### Phase 2
Search inside OCR text, folders, favorites, PDF merge/split/compression, password-protected PDF, app lock + biometrics, better table OCR, better Word formatting, cloud backup, Google Drive integration, OneDrive integration.

### Phase 3 (Advanced)
Digital signatures / e-signing, automatic invoice/receipt recognition, ID card / passport / business card scanning, AI document classification, AI auto-naming, AI summarization, AI "ask your document" Q&A (must be an explicit opt-in cloud feature with clear privacy disclosure), smart folders, cross-device cloud sync.

---

## 12. Monetization

| Free | Premium |
|---|---|
| Basic scanning, PDF export, JPG/PNG export, limited OCR | Unlimited OCR, Word export, advanced PDF tools, cloud backup, password-protected PDFs, document signing, advanced compression, no ads |

Avoid intrusive advertising, especially during the scanning of sensitive documents.

---

## 13. Testing Strategy

- **Unit tests:** OCR processing, PDF creation, file naming, document repository, search, page ordering, compression
- **UI tests:** scan flow, export flow, document management, sharing
- **Device tests:** low/mid/high-end Android, varying camera resolutions, multiple Android OS versions

---

## 14. Open Items Before Kickoff

- Final app name (candidates: SmartScan, DocScan AI, ScanFlow, ScanPro, DocuSnap) — verify trademark and Play Store name availability
- Minimum supported Android API level
- Choice of on-device DOCX generation library
- Confirm ML Kit language pack sizes are acceptable for offline install
