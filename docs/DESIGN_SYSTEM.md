# RetireWise — Design System

## 18.1 Design character

The application should feel: calm, trustworthy, modern, Canadian, intelligent, human, reassuring, clear, respectful, premium without feeling exclusive.

Avoid the appearance of: a stock trading application, a tax filing application, a government portal, a spreadsheet, a generic chatbot, a casino or gamified investment product.

## 18.2 Visual language

Generous spacing, rounded but not playful cards, clear typography, restrained use of colour, simple charts, plain language labels, prominent explanations, accessible contrast, soft motion, clear progress indicators.

## 18.3 Semantic colour tokens

Do not hardcode colours directly in feature screens. Define semantic tokens:

```
Primary
PrimaryContainer
Secondary
Background
Surface
SurfaceRaised
TextPrimary
TextSecondary
Success
Caution
Critical
Information
Divider
Disabled
ChartPositive
ChartNeutral
ChartNegative
```

Recommended design direction:

```
Primary: deep evergreen or navy
Secondary: muted teal
Background: warm white
Surface: white
Success: accessible green
Caution: amber
Critical: restrained red
```

Do not use red simply because a user is behind plan. Use compassionate language and avoid shame.

## 18.4 Typography

Use platform appropriate system fonts. Support Dynamic Type and Android font scaling.

Suggested hierarchy: Display, Page title, Section title, Card title, Body, Supporting text, Label, Financial figure, Button.

Financial figures should use tabular numerals where supported.

## 18.5 Accessibility

Meet or exceed WCAG 2.2 AA. Support: VoiceOver, TalkBack, Dynamic Type, font scaling, high contrast, reduced motion, screen reader descriptions for charts, large touch targets, keyboard navigation where applicable, clear error messages, no colour only communication, plain language.

## 18.6 Chart design

Charts must answer one clear question (e.g. "Will my savings last?"). Every chart must include: title, plain language takeaway, accessible data summary, assumptions link, data table alternative. Avoid dense legends and unnecessary animation.

## 18.7 AI conversation design

The AI screen should include: suggested starter questions, clear distinction between user input and AI response, expandable assumptions, source links, calculation cards, scenario cards, save to plan action, try another scenario action, explain this action, professional review indicator when appropriate.

Do not display long walls of text. Break responses into: direct answer, why, what this means for you, options, next action.

## Phase 0 status

Semantic colour tokens, a typography scale (display, headline, body, label, and financial-figure text styles using the platform default font family), and spacing tokens exist so far (`shared/design_system`), consumed by a single welcome screen. The full showcase screen, chart components, and AI conversation components are Phase 1+ work per `RELEASE_PLAN.md`.

## Phase 1 status

`RetireWiseButton` (Primary/Secondary variants, pill shape, accessible content descriptions, variant→style mapping unit tested independently of Compose), `RetireWiseCard` (moderate-radius bordered surface container, optional whole-card `onClick`; interactivity resolution is unit tested independently of Compose), and `RetireWiseTextField` (labelled outlined input with supporting/error text — error text always replaces supporting text rather than relying on colour alone, per section 18.5; the text resolution logic is unit tested independently of Compose) exist in `shared/design_system`. `RetireWiseButton` and `RetireWiseCard` are consumed by the welcome screen; `RetireWiseTextField` is not yet consumed by a screen. `RetireWiseRadioOption` (mutually-exclusive choice) and `RetireWiseCheckboxOption` (independent toggle) are stateless, controlled selection controls with the full row as an accessible tap target (radio/checkbox semantics role). `RetireWiseProgressBar` (determinate progress with a plain-language label and percentage, never colour/length alone) and `RetireWiseStepIndicator` (step-of-total dots for multi-step flows, always paired with a "Step X of Y" content description) also exist, with their formatting/colour-mapping logic unit tested independently of Compose. `RetireWiseChartContainer` (title, plain-language takeaway, accessible data summary exposed to screen readers in place of the raw visual, optional assumptions/data-table actions per section 18.6, with the action-visibility logic unit tested independently of Compose) also exists as an accessible scaffold — it does not draw a chart itself, since no charting library is bundled yet. `RetireWiseLoadingIndicator` (indeterminate spinner always paired with a plain-language label) and `RetireWiseErrorState` (title, plain-language message, optional retry action — the critical colour accent never carries meaning alone) complete the set, with their label/visibility logic unit tested independently of Compose. All eleven Phase 1 component sub-tasks are now done. A `DesignSystemShowcaseScreen` exercises every component above and runs on both Android and iOS, satisfying the Phase 1 exit criteria. It is reachable from Welcome via a small "Design system" text link and is verification scaffolding rather than a real product screen — see the doc comment on `Screen.DesignSystemShowcase` for removal/relocation guidance once Phase 2's navigation shell exists.

`RetireWiseEmptyState` (title, plain-language message, optional secondary action — informational tone via `colors.textPrimary`/`colors.textSecondary`, never the critical/caution accent) was added in Phase 2 to complete section 15's loading/empty/success/error requirement, which Phase 1 had left incomplete.

## Phase 2 status

`shared/navigation` now hosts the application shell: a `Destination` enum (Today/Plan/Explore/Learn/Ask AI) with route↔enum mapping unit tested independently of Compose, and `MainAppScaffold`, which wires all five destinations into an `androidx.navigation-compose` `NavHost` (ADR 0003) behind a `DestinationNavigationBar` (bottom, compact widths) or `DestinationNavigationRail` (side, expanded widths, ≥600dp — `usesNavigationRail` unit tested independently of Compose). Tab selection is signalled by both colour and type weight, never colour alone (section 18.5), via `destinationTabAppearance`, unit tested independently of Compose. System back at the root destination exits back to Welcome (`BackHandler`, `androidx.compose.ui.backhandler`) rather than doing nothing. Today, Learn, and Ask AI render `RetireWiseEmptyState` with screen-specific copy; Plan and Explore render static, structural lists of the categories/scenario types named in `PRD.md` sections 8.2/8.3 (`planCategories`/`exploreScenarioTypes`, each unit tested against the PRD list) ahead of any real data. All five destinations satisfy the Phase 2 exit criteria on both Android and iOS. `TodayScreen`, `LearnScreen`, `AskAiScreen`, `MainAppScaffold`, and the navigation bar/rail/tab-label composables have no dedicated Compose-level test, since all of their branching/mapping logic is already extracted into the pure functions above and unit tested there — the composables themselves are thin rendering/wiring, following the same precedent as `RetireWiseRadioOption`/`RetireWiseCheckboxOption` in Phase 1.
